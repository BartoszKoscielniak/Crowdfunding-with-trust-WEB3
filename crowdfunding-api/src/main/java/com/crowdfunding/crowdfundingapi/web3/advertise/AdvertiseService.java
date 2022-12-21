package com.crowdfunding.crowdfundingapi.web3.advertise;

import com.crowdfunding.crowdfundingapi.collection.CollectionService;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhaseService;
import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import com.crowdfunding.crowdfundingapi.user.UserService;
import com.crowdfunding.crowdfundingapi.web3.Web3;
import com.crowdfunding.crowdfundingapi.web3.Web3Repository;
import com.crowdfunding.crowdfundingapi.web3.Web3Service;
import com.crowdfunding.crowdfundingapi.web3.wrappedcontracts.Advertise;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.*;

@Service
@AllArgsConstructor
public class AdvertiseService {

    private final Web3j web3j = new Web3().getWeb3j();
    private final Web3Repository repository;
    private final CollectionPhaseService collectionPhaseService;
    private final Web3Service web3Service;
    private final CollectionService collectionService;
    private final UserService userService;
    private final static String CONTRACT_NAME = "Advertise";

    private Advertise loadFundsContract( ) throws Exception {
        Optional<Web3> fundsContract = repository.findContractByName(CONTRACT_NAME);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();

        if (fundsContract.isEmpty()){
            String contractAddress = Advertise.deploy(
                    web3j,
                    web3Service.getCrowdfundingCredentials(),
                    contractGasProvider
            ).send().getContractAddress();
            web3Service.setContractAddress(CONTRACT_NAME, contractAddress);
        }

        Advertise contract = Advertise.load(
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

    public ResponseEntity<Map<String, String>> buyAdvertise(Long collectionId, Long advertiseId ) {//TODO: sprawdzanie czy uzytkownik ma zbiorke
        try {
            Advertise advertiseContract = loadFundsContract();

            Function function = new Function(
                    "buyAdvertisement",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(collectionId),
                            new org.web3j.abi.datatypes.generated.Uint256(advertiseId)),
                    List.<TypeReference<?>>of()
            );

            Tuple3<String, BigInteger, BigInteger> adType = getAdInfo(advertiseId).getBody();
            if (Objects.equals(adType.component3(), BigInteger.ZERO)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Advertise type not found"));
            }

            return web3Service.sendPayableFunction(function, advertiseContract.getContractAddress(), adType.component2());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> addType( String name, Double price, int duration ) {
        try {
            Advertise advertiseContract = loadFundsContract();
            duration = duration * 24 * 3600;
            TransactionReceipt receipt = advertiseContract.addAdType(name, Convert.toWei(String.valueOf(price), Convert.Unit.ETHER).toBigInteger(), BigInteger.valueOf(duration)).send();
            if (receipt.isStatusOK()){
                return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(receipt.getStatus()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getSuccessResponse(receipt.getStatus()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<List<Tuple3<String, BigInteger, BigInteger>>> getAdvertiseTypes() {
        try {
            Advertise advertiseContract = loadFundsContract();

            BigInteger adTypesQuantity = advertiseContract.availableAdPlansId().send();
            List<Tuple3<String, BigInteger, BigInteger>> types = new ArrayList<>();
            for (int i = 0; i < adTypesQuantity.intValue(); i++) {
                Tuple3<String, BigInteger, BigInteger> receipt = advertiseContract.advertiseType(BigInteger.valueOf(i)).send();
                types.add(receipt);
            }

            return ResponseEntity.status(HttpStatus.OK).body(types);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    private ResponseEntity<Tuple3<String, BigInteger, BigInteger>> getAdInfo(Long adId){
        try {

            Advertise advertiseContract = loadFundsContract();
            Tuple3<String, BigInteger, BigInteger> type = advertiseContract.advertiseType(BigInteger.valueOf(adId)).send();

            return ResponseEntity.status(HttpStatus.OK).body(type);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }
}
