package com.crowdfunding.crowdfundingapi.web3.funds;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.collection.CollectionService;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhaseService;
import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import com.crowdfunding.crowdfundingapi.support.CollUserRelation;
import com.crowdfunding.crowdfundingapi.support.CollUserType;
import com.crowdfunding.crowdfundingapi.support.RelationRepository;
import com.crowdfunding.crowdfundingapi.user.User;
import com.crowdfunding.crowdfundingapi.user.UserService;
import com.crowdfunding.crowdfundingapi.web3.Web3;
import com.crowdfunding.crowdfundingapi.web3.Web3Repository;
import com.crowdfunding.crowdfundingapi.web3.Web3Service;
import com.crowdfunding.crowdfundingapi.web3.commission.CommissionService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.text.WordUtils;
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
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.*;

@Service
@AllArgsConstructor
public class FundsService{

    private final Web3j web3j = new Web3().getWeb3j();
    private final Web3Repository repository;
    private final CollectionPhaseService collectionPhaseService;
    private final Web3Service web3Service;
    private final CollectionService collectionService;
    private final UserService userService;
    private final RelationRepository relationRepository;
    private final static String CONTRACT_NAME = "Funds";
    private final CommissionService commissionService;

    private Funds loadFundsContract( ) throws Exception {
        Optional<Web3> fundsContract = repository.findContractByName(CONTRACT_NAME);
        EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock();
        BigInteger baseFeePerGas = Numeric.toBigInt(block.getBaseFeePerGas());
        ContractGasProvider contractGasProvider = new StaticGasProvider(baseFeePerGas.multiply(BigInteger.valueOf(3)), DefaultGasProvider.GAS_LIMIT);
        if (fundsContract.isEmpty()){
            String contractAddress = Funds.deploy(
                    web3j,
                    web3Service.getCrowdfundingCredentials(),
                    contractGasProvider
            ).send().getContractAddress();
            web3Service.setContractAddress(CONTRACT_NAME, contractAddress);
        }

        Funds contract = Funds.load(
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

    public ResponseEntity<Map<String, String>> depositFunds(Long collectionId, Double amount) {
        try {
            Collection collection = collectionService.getCollection(collectionId).getBody();
            if (collection == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Collection not found"));
            }

            if (amount <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Incorrect amount"));
            }

            Long phaseId = collection.getCollectionPhase().get(0).getId();
            String receiverAddress = Objects.requireNonNull(collectionPhaseService.getCollectionFounder(phaseId).getBody()).getPublicAddress();
            BigInteger convertedAmount = Convert.toWei(String.valueOf(amount), Convert.Unit.ETHER).toBigInteger();
            BigInteger commission = commissionService.getCommissionAmountBigInteger(amount, collection.getCollectionType());
            User user = userService.getUserFromAuthentication();
            Funds contract = loadFundsContract();

            TransactionReceiptProcessor receipt = new PollingTransactionReceiptProcessor(web3j, TransactionManager.DEFAULT_POLLING_FREQUENCY, TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);
            ResponseEntity<Map<String, String>> result = commissionService.payCommission(amount, collection.getCollectionType());
            if (result.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse(result.getBody().get("error")));
            }
            receipt.waitForTransactionReceipt(result.getBody().get("result"));

            convertedAmount = convertedAmount.subtract(commission);
            Function function = new Function(
                    "depositFunds",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(receiverAddress),
                            new org.web3j.abi.datatypes.generated.Uint256(convertedAmount),
                            new org.web3j.abi.datatypes.generated.Uint256(BigInteger.valueOf(collectionId))),
                    List.<TypeReference<?>>of()
            );

            ResponseEntity<Map<String, String>> response = web3Service.sendPayableFunction(function ,contract.getContractAddress(), convertedAmount);
            if (response.getStatusCode() != HttpStatus.OK){
                if (commission.compareTo(BigInteger.valueOf(0)) == 0){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getBody());
                }
                ResponseEntity<Map<String, String>> refundResult = web3Service.sendEthTransaction(user.getPublicAddress(), web3Service.getUserCredentials(), commission);
                if (refundResult.getStatusCode() != HttpStatus.OK){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse(WordUtils.capitalize(response.getBody().get("error")) + ". Commission refund has failed"));
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse(WordUtils.capitalize(response.getBody().get("error"))));
            }
            List<CollUserRelation> relations = collection.getCollUserRelations();
            CollUserRelation collUserRelation = new CollUserRelation(user, collection, CollUserType.SUSTAINER);
            if (relations.contains(collUserRelation)){
                return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
            }
            relationRepository.save(collUserRelation);
            return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> setCollectionFraud(Long collectionId) {
        try {
            ResponseEntity<Collection> collection = collectionService.getCollection(collectionId);
            if (collection.getStatusCode() == HttpStatus.NOT_FOUND){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            Funds contract = loadFundsContract();
            TransactionReceipt response = contract.setFraud(BigInteger.valueOf(collectionId)).send();
            if (response.isStatusOK()){
                return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Collection setted as fraud"));
            }
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new PreparedResponse().getFailureResponse(response.getStatus()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> setCollectionPollEnd(Long collectionId) {
        try {
            ResponseEntity<Collection> collection = collectionService.getCollection(collectionId);
            if (collection.getStatusCode() == HttpStatus.NOT_FOUND){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            Funds contract = loadFundsContract();
            TransactionReceipt response = contract.setPollEnded(BigInteger.valueOf(collectionId)).send();
            if (response.isStatusOK()){
                return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Collection poll ended"));
            }
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new PreparedResponse().getFailureResponse(response.getStatus()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> getTransactionHistory() {
        try {
            Funds contract = loadFundsContract();

            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(contract.getTransactionHiostory().send().toString()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    public ResponseEntity<Map<String, String>> sendFundsToOwner(Long collectionId) {
        try {
            ResponseEntity<Collection> collection = collectionService.getCollection(collectionId);
            if (collection.getStatusCode() == HttpStatus.NOT_FOUND){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            Funds contract = loadFundsContract();
            TransactionReceipt response = contract.sendFundsToOwner(userService.getUserFromAuthentication().getPublicAddress(), BigInteger.valueOf(collectionId)).send();
            if (response.isStatusOK()){
                return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Funds has been send"));
            }
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new PreparedResponse().getFailureResponse(response.getStatus()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> sendFundsToDonators(Long collectionId, List<Long> transactionsIdList) {
        try{
            ResponseEntity<Collection> collection = collectionService.getCollection(collectionId);
            if (collection.getStatusCode() == HttpStatus.NOT_FOUND){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            Funds contract = loadFundsContract();

            if (transactionsIdList.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Provide transaction ID"));
            }

            List<BigInteger> transactionsList = new ArrayList<>();
            transactionsIdList.forEach(id -> {
                transactionsList.add(BigInteger.valueOf(id));
            });

            TransactionReceipt response = contract.sendFundsToDonators(userService.getUserFromAuthentication().getPublicAddress(), BigInteger.valueOf(collectionId), transactionsList).send();
            if (response.isStatusOK()){
                return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Collection poll ended"));
            }
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new PreparedResponse().getFailureResponse(response.getStatus()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> getDonatedFunds(Long collectionId) {
        try {
            Funds contract = loadFundsContract();
            List<Tuple5<String, BigInteger, Boolean, Boolean, BigInteger>> response = Collections.singletonList(contract.fundsDonated(BigInteger.valueOf(collectionId)).send());
            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(response.toString()));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    public ResponseEntity<Map<String, String>> isFraud(Long collectionId){
        try {
            ResponseEntity<Collection> collection = collectionService.getCollection(collectionId);
            if (collection.getStatusCode() == HttpStatus.NOT_FOUND){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Funds contract = loadFundsContract();
            Boolean isFraud = contract.isFraud(BigInteger.valueOf(collectionId)).send();
            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(String.valueOf(isFraud)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> isPollEnded(Long collectionId){
        try {
            ResponseEntity<Collection> collection = collectionService.getCollection(collectionId);
            if (collection.getStatusCode() == HttpStatus.NOT_FOUND){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Funds contract = loadFundsContract();
            Boolean isPollEnded = contract.isPollEnded(BigInteger.valueOf(collectionId)).send();
            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(String.valueOf(isPollEnded)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }
}
