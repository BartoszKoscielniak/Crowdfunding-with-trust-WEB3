package com.crowdfunding.crowdfundingapi.web3.commission;

import com.crowdfunding.crowdfundingapi.collection.CollectionType;
import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import com.crowdfunding.crowdfundingapi.user.UserService;
import com.crowdfunding.crowdfundingapi.web3.Web3;
import com.crowdfunding.crowdfundingapi.web3.Web3Repository;
import com.crowdfunding.crowdfundingapi.web3.Web3Service;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommissionService {//TODO:zlapac wyjatki wszystkie

    private final Web3Repository repository;
    private final Web3j web3j = new Web3().getWeb3j();
    private final Web3Service web3Service;
    private final static String CONTRACT_NAME = "Commission";
    private final UserService userService;

    private Commission loadFundsContract( ) throws Exception {
        Optional<Web3> fundsContract = repository.findContractByName(CONTRACT_NAME);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();

        if (fundsContract.isEmpty()){
            String contractAddress = Commission.deploy(
                    web3j,
                    web3Service.getCrowdfundingCredentials(),
                    contractGasProvider
            ).send().getContractAddress();
            web3Service.setContractAddress(CONTRACT_NAME, contractAddress);
        }

        Commission contract = Commission.load(
                repository.findContractByName(CONTRACT_NAME).get().getContractAddress(),
                web3j,
                web3Service.getUserCredentials(),
                contractGasProvider
        );

        if (!contract.isValid()){
            repository.delete(fundsContract.get());
            return loadFundsContract();
        }

        return contract;
    }

    public ResponseEntity<Map<String, String>> getCommissionBalance() {
        try {
            Commission commission = loadFundsContract();
            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(String.valueOf(commission.getBalance().send())));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> withdrawCommission() {
        TransactionReceipt receipt = null;
        try {
            Commission commission = loadFundsContract();
            receipt = commission.withdrawAll(userService.getUserFromAuthentication().getPublicAddress()).send();
            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(receipt.getStatus()));
        } catch (Exception exception) {
            assert receipt != null;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse(exception.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> payCommission(Double amount, CollectionType type)  {
        try {
            Commission contract = loadFundsContract();

            BigInteger commissionAmount = getCommissionAmountBigInteger(amount, type);

            Function function = new Function(
                    "payCommission",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(Convert.toWei(amount.toString(),
                                    Convert.Unit.ETHER).toBigInteger()),
                                    new org.web3j.abi.datatypes.generated.Uint256(commissionAmount)),
                    List.<TypeReference<?>>of()
            );

            return web3Service.sendPayableFunction(function, contract.getContractAddress(), commissionAmount);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> getCommissionAmount(Double amount, CollectionType type) {
        try {
            Commission commission = loadFundsContract();
            BigInteger receipt = commission.getCommissionRate(Convert.toWei(amount.toString(), Convert.Unit.ETHER).toBigInteger(), type.toString()).send();
            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(String.valueOf(Convert.fromWei(String.valueOf(receipt), Convert.Unit.ETHER))));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public BigInteger getCommissionAmountBigInteger(Double amount, CollectionType type) throws Exception {
        Commission commission = loadFundsContract();
        return commission.getCommissionRate(Convert.toWei(amount.toString(), Convert.Unit.ETHER).toBigInteger(), type.toString()).send();
    }
}
