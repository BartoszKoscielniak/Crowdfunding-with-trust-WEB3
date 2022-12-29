package com.crowdfunding.crowdfundingapi.web3.advertise;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.collection.CollectionRepository;
import com.crowdfunding.crowdfundingapi.collection.CollectionService;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhaseService;
import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import com.crowdfunding.crowdfundingapi.user.User;
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
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.sql.Timestamp;
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
    private final CollectionRepository collectionRepository;
    private final static String CONTRACT_NAME = "Advertise";

    private Advertise loadFundsContract( ) throws Exception {
        Optional<Web3> fundsContract = repository.findContractByName(CONTRACT_NAME);
        EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock();
        BigInteger baseFeePerGas = Numeric.toBigInt(block.getBaseFeePerGas());
        ContractGasProvider contractGasProvider = new StaticGasProvider(baseFeePerGas.multiply(BigInteger.valueOf(3)), DefaultGasProvider.GAS_LIMIT);
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

    public ResponseEntity<Map<String, String>> buyAdvertise(Long collectionId, Long advertiseId ) {
        try {
            ResponseEntity<Collection> collectionEntity = collectionService.getCollection(collectionId);
            if (collectionEntity.getStatusCode() != HttpStatus.OK){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Collection not found"));
            }

            Collection collection = collectionEntity.getBody();
            User user = collectionPhaseService.getCollectionFounder(collection.getCollectionPhase().get(0).getId()).getBody();
            if (!user.getPublicAddress().equals(userService.getUserFromAuthentication().getPublicAddress())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Only collection owner can buy advertise"));
            }

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

            ResponseEntity<Map<String, String>> response = web3Service.sendPayableFunction(function, advertiseContract.getContractAddress(), adType.component2());
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST){
                return response;
            }

            BigInteger tillTimestamp = getBoughtAds(collectionId).getBody().component2();
            collection.setPromoted(true);
            collection.setPromoTo(Timestamp.valueOf(tillTimestamp.toString()).toLocalDateTime());
            collectionRepository.save(collection);

            return response;
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

    public ResponseEntity<Map<String, String>> getAdvertiseTypes() {
        try {
            Advertise advertiseContract = loadFundsContract();

            BigInteger adTypesQuantity = advertiseContract.availableAdPlansId().send();
            List<Tuple3<String, BigInteger, BigInteger>> types = new ArrayList<>();
            for (int i = 0; i < adTypesQuantity.intValue(); i++) {
                Tuple3<String, BigInteger, BigInteger> receipt = advertiseContract.advertiseType(BigInteger.valueOf(i)).send();
                types.add(receipt);
            }

            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(types.toString()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    public ResponseEntity<Map<String, String>> getHistory() {
        try {
            Advertise advertiseContract = loadFundsContract();

            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(advertiseContract.getTransactionHiostory().send().toString()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    private ResponseEntity<Tuple3<String, BigInteger, BigInteger>> getBoughtAds(Long collectionId){
        try {
            Advertise advertiseContract = loadFundsContract();

            return ResponseEntity.status(HttpStatus.OK).body(advertiseContract.advertiseBought(BigInteger.valueOf(collectionId)).send());
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
