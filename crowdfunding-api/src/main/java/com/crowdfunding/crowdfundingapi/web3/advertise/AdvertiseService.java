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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
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

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Service
@AllArgsConstructor
@EnableScheduling
public class AdvertiseService {

    private final Web3j web3j = new Web3().getWeb3j();
    private final Web3Repository repository;
    private final CollectionPhaseService collectionPhaseService;
    private final Web3Service web3Service;
    private final CollectionService collectionService;
    private final UserService userService;
    private final CollectionRepository collectionRepository;
    private final static String CONTRACT_NAME = "Advertise";

    public static class TransactionStruct {
        public String sender;
        public String receiver;
        public Integer collectionId;
        public String collectionName;
        public BigDecimal amount;
        public Integer adTypeId;
        public String adTypeName;
        public LocalDateTime promoTo;
        public LocalDateTime timeOfTransaction;

        public TransactionStruct(String sender, String receiver, String collectionId, String collectionName, BigDecimal amount, Integer adTypeId, String adTypeName, LocalDateTime promoTo, LocalDateTime timeOfTransaction) {
            this.sender = sender;
            this.receiver = receiver;
            this.collectionId = Integer.valueOf(collectionId);
            this.collectionName = collectionName;
            this.amount = amount;
            this.adTypeId = adTypeId;
            this.adTypeName = adTypeName;
            this.promoTo = promoTo;
            this.timeOfTransaction = timeOfTransaction;
        }
    }

    public static class AdTypeStruct {
        public String name;
        public BigDecimal price;
        public BigInteger duration;

        public AdTypeStruct(String name, BigDecimal price, BigInteger duration) {
            this.name = name;
            this.price = price;
            this.duration = duration;
        }
    }

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

            BigInteger tillTimestamp = getBoughtAds(collectionId).getBody().component2();//do kiedy
            BigInteger days = adType.component3().divide(BigInteger.valueOf(24)).divide(BigInteger.valueOf(3600));//duration
            LocalDateTime promoTo;
            if (tillTimestamp.compareTo(BigInteger.valueOf(0)) == 0){
                promoTo = LocalDateTime.now().withHour(23).withMinute(59).withSecond(0).plusDays(days.longValue());
            }else{
                promoTo = LocalDateTime.ofEpochSecond(Long.parseLong(tillTimestamp.toString()), 0, ZoneOffset.UTC).withHour(23).withMinute(59).withSecond(0).plusDays(days.longValue());
            }

            ResponseEntity<Map<String, String>> response = web3Service.sendPayableFunction(function, advertiseContract.getContractAddress(), adType.component2());
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST){
                return response;
            }

            collection.setPromoted(true);
            collection.setPromoTo(promoTo);
            collectionRepository.save(collection);

            return response;
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> addType( String name, Double price, int duration ) {
        try {
            if (duration < 1){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getSuccessResponse("Duration have to be at least 1 day"));
            }

            if (price < 0.001){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getSuccessResponse("Price have to be higher than 0.001 ETH"));
            }

            if (name.length() < 3){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getSuccessResponse("Name length have to be longer than 3 characters"));
            }

            ResponseEntity<List<AdTypeStruct>> adTypes = getAdvertiseTypes();
            if (adTypes.getStatusCode() != HttpStatus.OK){
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
            }

            BigInteger convertedPrice = Convert.toWei(String.valueOf(price), Convert.Unit.ETHER).toBigInteger();
            BigInteger convertedDuration = BigInteger.valueOf(duration * 24 * 3600);
            for (int i = 0; i < adTypes.getBody().size(); i++){
                if (adTypes.getBody().get(i).name.equals(name)){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Ad type with provided name exist!"));
                }

                if (Objects.equals(adTypes.getBody().get(i).price, convertedPrice) && Objects.equals(adTypes.getBody().get(i).duration, convertedDuration)){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Ad type with same price and duration exist"));
                }
            }


            Advertise advertiseContract = loadFundsContract();
            TransactionReceipt receipt = advertiseContract.addAdType(name, convertedPrice, convertedDuration).send();
            if (receipt.isStatusOK()){
                return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(receipt.getTransactionHash()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse(receipt.getTransactionHash()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<List<AdTypeStruct>> getAdvertiseTypes() {
        try {
            Advertise advertiseContract = loadFundsContract();

            BigInteger adTypesQuantity = advertiseContract.availableAdPlansId().send();
            List<AdvertiseService.AdTypeStruct> types = new ArrayList<>();
            for (int i = 0; i < adTypesQuantity.intValue(); i++) {
                Tuple3<String, BigInteger, BigInteger> receipt = advertiseContract.advertiseType(BigInteger.valueOf(i)).send();
                types.add(new AdTypeStruct(
                        receipt.component1(),
                        Convert.fromWei(String.valueOf(receipt.component2()), Convert.Unit.ETHER),
                        receipt.component3().divide(BigInteger.valueOf(24)).divide(BigInteger.valueOf(3600))
                ));
            }

            return ResponseEntity.status(HttpStatus.OK).body(types);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    public ResponseEntity<List<AdvertiseService.TransactionStruct>> getHistory() {
        try {
            Advertise advertiseContract = loadFundsContract();
            List advertiseTransactions = advertiseContract.getTransactionHiostory().send();
            List<AdvertiseService.TransactionStruct> formattedData = new ArrayList<>();
            if (advertiseTransactions.size() == 0){
                return ResponseEntity.status(HttpStatus.OK).body(formattedData);
            }

            ResponseEntity<List<AdTypeStruct>> adTypes = getAdvertiseTypes();
            if (adTypes.getStatusCode() != HttpStatus.OK){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            for (Object object : advertiseTransactions) {
                Class<?> struct = object.getClass();
                Field senderField = struct.getField("sender");
                Object senderValue = senderField.get(object);
                Field receiverField = struct.getField("receiver");
                Object receiverValue = receiverField.get(object);
                Field collectionIdField = struct.getField("_collectionId");
                Object collectionIdValue = collectionIdField.get(object);
                Field amountField = struct.getField("amount");
                Object amountValue = amountField.get(object);
                Field adTypeField = struct.getField("_adType");
                Object adTypeValue = adTypeField.get(object);
                Field promoToField = struct.getField("_promoTo");
                Object promoToValue = promoToField.get(object);
                Field timestampField = struct.getField("timestamp");
                Object timestampValue = timestampField.get(object);

                ResponseEntity<Collection> collectionResponse = collectionService.getCollection(Long.valueOf(collectionIdValue.toString()));
                String collectionName = "";
                if (collectionResponse.getStatusCode() == HttpStatus.OK){
                    collectionName = collectionResponse.getBody().getCollectionName();
                }
                LocalDateTime timestamp = LocalDateTime.ofEpochSecond(Long.parseLong(timestampValue.toString()), 0, ZoneId.of("Europe/Warsaw").getRules().getOffset(LocalDateTime.now()));
                LocalDateTime promoTo = LocalDateTime.ofEpochSecond(Long.parseLong(promoToValue.toString()), 0, ZoneId.of("Europe/Warsaw").getRules().getOffset(LocalDateTime.now())).withHour(23).withMinute(59).withSecond(0);
                BigDecimal parsedValue = Convert.fromWei(String.valueOf(amountValue), Convert.Unit.ETHER);

                formattedData.add(new AdvertiseService.TransactionStruct(
                        senderValue.toString(),
                        receiverValue.toString(),
                        collectionIdValue.toString(),
                        collectionName,
                        parsedValue,
                        Integer.parseInt(adTypeValue.toString()),
                        adTypes.getBody().get(Integer.parseInt(adTypeValue.toString())).name,
                        promoTo,
                        timestamp
                ));
            }

            return ResponseEntity.status(HttpStatus.OK).body(formattedData);
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

    public ResponseEntity<Map<String, String>> withdrawAdFunds() {
        TransactionReceipt receipt = null;
        try {
            Advertise advertise = loadFundsContract();

            BigInteger minimalPayout = Convert.toWei("0.05", Convert.Unit.ETHER).toBigInteger();
            if (advertise.getBalance().send().compareTo(minimalPayout) < 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Minimum payout of 0.05 ETH is not reached"));
            }

            receipt = advertise.withdrawAll(userService.getUserFromAuthentication().getPublicAddress()).send();
            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(receipt.getTransactionHash()));
        } catch (Exception exception) {
            assert receipt != null;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse(exception.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> getAdvertiseBalance() {
        try {
            Advertise advertise = loadFundsContract();
            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(String.valueOf(Convert.fromWei(String.valueOf(advertise.getBalance().send()), Convert.Unit.ETHER))));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    @Scheduled(cron = "30 0 0 * * *")
    public void checkCollectionsPromoting(){
        List<Collection> collections = collectionRepository.findAll();
        collections.forEach(collection -> {
            if (collection.getPromoTo() != null && collection.getPromoTo().isBefore(LocalDateTime.now())){
                collection.setPromoted(false);
                collectionRepository.save(collection);
            }
        });
    }
}
