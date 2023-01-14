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
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@AllArgsConstructor
public class CommissionService {

    private final Web3Repository repository;
    private final Web3j web3j = new Web3().getWeb3j();
    private final Web3Service web3Service;
    private final static String CONTRACT_NAME = "Commission";
    private final UserService userService;

    public static class TransactionStruct {
        public String sender;
        public BigDecimal amount;
        public String commissinon;
        public String date;

        public TransactionStruct(String sender, BigDecimal amount, String commissinon, String date) {
            this.sender = sender;
            this.amount = amount;
            this.commissinon = commissinon;
            this.date = date;
        }
    }

    private Commission loadFundsContract( ) throws Exception {
        Optional<Web3> fundsContract = repository.findContractByName(CONTRACT_NAME);
        EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock();
        BigInteger baseFeePerGas = Numeric.toBigInt(block.getBaseFeePerGas());
        ContractGasProvider contractGasProvider = new StaticGasProvider(baseFeePerGas.multiply(BigInteger.valueOf(3)), DefaultGasProvider.GAS_LIMIT);
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
            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(String.valueOf(Convert.fromWei(String.valueOf(commission.getBalance().send()), Convert.Unit.ETHER))));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<List<CommissionService.TransactionStruct>> getHistory() {
        try {
            Commission commission = loadFundsContract();

            List commissionTransactions = commission.getCommissionHistory().send();
            List<CommissionService.TransactionStruct> formattedData = new ArrayList<>();
            if (commissionTransactions.size() == 0) {
                return ResponseEntity.status(HttpStatus.OK).body(formattedData);
            }

            for (Object object : commissionTransactions) {
                Class<?> struct = object.getClass();
                Field senderField = struct.getField("sender");
                Object senderValue = senderField.get(object);
                Field amountField = struct.getField("amount");
                Object amountValue = amountField.get(object);
                Field commissionField = struct.getField("commission");
                Object commissionValue = commissionField.get(object);
                Field timestampField = struct.getField("timestamp");
                Object timestampValue = timestampField.get(object);

                LocalDateTime time = LocalDateTime.ofEpochSecond(Long.parseLong(timestampValue.toString()), 0, ZoneOffset.UTC);//TODO: strefa czasowa
                BigDecimal parsedAmount = Convert.fromWei(String.valueOf(amountValue), Convert.Unit.ETHER);
                BigDecimal parsedCommission = Convert.fromWei(String.valueOf(commissionValue), Convert.Unit.ETHER);

                formattedData.add(new CommissionService.TransactionStruct(
                        senderValue.toString(),
                        parsedAmount,
                        parsedCommission.toPlainString(),
                        time.toString()
                ));
            }

            return ResponseEntity.status(HttpStatus.OK).body(formattedData);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    public ResponseEntity<Map<String, String>> withdrawCommission() {
        TransactionReceipt receipt = null;
        try {
            Commission commission = loadFundsContract();
            receipt = commission.withdrawAll(userService.getUserFromAuthentication().getPublicAddress()).send();
            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(receipt.getTransactionHash()));
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
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(Convert.toWei(amount.toString(), Convert.Unit.ETHER).toBigInteger()),
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
