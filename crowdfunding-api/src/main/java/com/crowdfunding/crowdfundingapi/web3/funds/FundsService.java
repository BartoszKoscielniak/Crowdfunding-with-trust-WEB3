package com.crowdfunding.crowdfundingapi.web3.funds;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.collection.CollectionRepository;
import com.crowdfunding.crowdfundingapi.collection.CollectionService;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhase;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhaseRepository;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhaseService;
import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import com.crowdfunding.crowdfundingapi.poll.Poll;
import com.crowdfunding.crowdfundingapi.poll.PollRepository;
import com.crowdfunding.crowdfundingapi.poll.PollState;
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

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private final CollectionPhaseRepository collectionPhaseRepository;
    private final CollectionRepository collectionRepository;
    private final PollRepository pollRepository;

    private ContractGasProvider getContractGasProvider() throws IOException {
        EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock();
        BigInteger baseFeePerGas = Numeric.toBigInt(block.getBaseFeePerGas());
        return new StaticGasProvider(baseFeePerGas.multiply(BigInteger.valueOf(3)), DefaultGasProvider.GAS_LIMIT);
    }

    private Funds loadFundsContract( ) throws Exception {
        Optional<Web3> fundsContract = repository.findContractByName(CONTRACT_NAME);
        ContractGasProvider contractGasProvider = getContractGasProvider();
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

    public static class TransactionStruct {
        public String sender;
        public String receiver;
        public String phaseId;
        public String phaseName;
        public BigDecimal amount;
        public String date;
        public TransactionStruct(String sender, String receiver, String phaseName, String phaseId, BigDecimal amount, String date) {
            this.sender = sender;
            this.receiver = receiver;
            this.phaseName = phaseName;
            this.amount = amount;
            this.date = date;
            this.phaseId = phaseId;
        }
    }

    public ResponseEntity<Map<String, String>> depositFunds(Long phaseId, Double amount) {
        try {
            CollectionPhase collectionPhase = collectionPhaseService.getPhase(phaseId).getBody();
            if (collectionPhase == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Phase not found!"));
            }

            ResponseEntity<User> founder = collectionPhaseService.getCollectionFounder(phaseId);
            User user = userService.getUserFromAuthentication();
            if (founder.getStatusCode() == HttpStatus.OK){
                if (founder.getBody().getPublicAddress().equals(user.getPublicAddress())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Cannot deposit funds to own collection!"));
                }
            }

            if (collectionPhase.getTill().isBefore(LocalDateTime.now())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Phase has ended!"));
            }

            if (amount < 0.005) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Minimum deposit is 0.005 ETH"));
            }

            Collection collection = collectionPhase.getCollection();
            String receiverAddress = Objects.requireNonNull(collectionPhaseService.getCollectionFounder(phaseId).getBody()).getPublicAddress();
            BigInteger convertedAmount = Convert.toWei(String.valueOf(amount), Convert.Unit.ETHER).toBigInteger();
            BigInteger commission = commissionService.getCommissionAmountBigInteger(amount, collectionPhase.getCollection().getCollectionType());
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
                            new org.web3j.abi.datatypes.generated.Uint256(BigInteger.valueOf(phaseId))),
                    List.<TypeReference<?>>of()
            );

            ResponseEntity<Map<String, String>> response = web3Service.sendPayableFunction(function ,contract.getContractAddress(), convertedAmount);
            if (response.getStatusCode() != HttpStatus.OK){
                if (commission.compareTo(BigInteger.valueOf(0)) == 0){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getBody());
                }
                ResponseEntity<Map<String, String>> refundResult = web3Service.sendRefund(user.getPublicAddress(), web3Service.getUserCredentials(), commission);
                if (refundResult.getStatusCode() != HttpStatus.OK){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse(WordUtils.capitalize(response.getBody().get("error")) + ". Commission refund has failed"));
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse(WordUtils.capitalize(response.getBody().get("error"))));
            }

            Optional<Poll> poll = pollRepository.findPollByPhaseId(phaseId);
            if (poll.isEmpty()){
                pollRepository.save(new Poll(PollState.NOT_ACTIVATED, collectionPhase, collectionPhase.getTill()));
            }

            collectionPhase.setActualFunds(collectionPhase.getActualFunds() + amount);
            collectionPhaseRepository.save(collectionPhase);

            collection.setActualFunds(collection.getActualFunds() + amount);
            collectionRepository.save(collection);

            List<CollUserRelation> relations = collection.getCollUserRelations();
            CollUserRelation collUserRelation = new CollUserRelation(user, collection, collectionPhase, CollUserType.SUSTAINER);
            AtomicBoolean assigned = new AtomicBoolean(false);
            relations.forEach(relation -> {
                if (relation.getType() == collUserRelation.getType() && relation.getUser() == collUserRelation.getUser()
                        && relation.getCollectionRelation() == collUserRelation.getCollectionRelation() && relation.getPhase() != null
                        && relation.getPhase() == collUserRelation.getPhase()){
                    assigned.set(true);
                }
            });
            if (assigned.get()){
                return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
            }
            relationRepository.save(collUserRelation);
            if (poll.isPresent()) {
                poll.get().setAllowedUsersCount(poll.get().getAllowedUsersCount() + 1);
                pollRepository.save(poll.get());
            }
            return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> setPollFraud(Long phaseId) {
        try {
            Optional<CollectionPhase> phase = collectionPhaseRepository.findPhaseById(phaseId);
            if (phase.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Phase not found!"));
            }

            ContractGasProvider contractGasProvider = getContractGasProvider();
            Funds contract = Funds.load(
                    repository.findContractByName(CONTRACT_NAME).get().getContractAddress(),
                    web3j,
                    web3Service.getCrowdfundingCredentials(),
                    contractGasProvider
            );

            TransactionReceipt response = contract.setFraud(BigInteger.valueOf(phaseId)).send();
            if (response.isStatusOK()){
                return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Collection setted as fraud"));
            }
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new PreparedResponse().getFailureResponse(response.getStatus()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> setPollEnd(Long phaseId) {
        try {
            Optional<CollectionPhase> phase = collectionPhaseRepository.findPhaseById(phaseId);
            if (phase.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Phase not found!"));
            }
            ContractGasProvider contractGasProvider = getContractGasProvider();
            Funds contract = Funds.load(
                    repository.findContractByName(CONTRACT_NAME).get().getContractAddress(),
                    web3j,
                    web3Service.getCrowdfundingCredentials(),
                    contractGasProvider
            );
            TransactionReceipt response = contract.setPollEnded(BigInteger.valueOf(phaseId)).send();
            if (response.isStatusOK()){
                return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Collection poll ended"));
            }
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new PreparedResponse().getFailureResponse(response.getStatus()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<List<FundsService.TransactionStruct>> getTransactionHistory(Boolean allTransactions) {
        try {
            User authUser = userService.getUserFromAuthentication();
            Funds contract = loadFundsContract();
            List transactions = contract.getTransactionHiostory().send();
            List<FundsService.TransactionStruct> formattedData = new ArrayList<>();
            if (transactions.size() == 0){
                return ResponseEntity.status(HttpStatus.OK).body(formattedData);
            }

            for (Object object : transactions) {
                Class<?> struct = object.getClass();
                Field senderField = struct.getField("sender");
                Object senderValue = senderField.get(object);
                if (!authUser.getPublicAddress().equalsIgnoreCase(senderValue.toString()) && !allTransactions){
                    continue;
                }
                Field receiverField = struct.getField("receiver");
                Object receiverValue = receiverField.get(object);
                Field phaseIdField = struct.getField("_phaseId");
                Object phaseIdValue = phaseIdField.get(object);
                Field amountField = struct.getField("amount");
                Object amountValue = amountField.get(object);
                Field timestampField = struct.getField("timestamp");
                Object timestampValue = timestampField.get(object);

                Optional<CollectionPhase> phaseResponse = collectionPhaseRepository.findPhaseById(Long.valueOf(phaseIdValue.toString()));
                String phaseName = "";
                if (phaseResponse.isPresent()){
                    phaseName = phaseResponse.get().getCollection().getCollectionName() + " - " + phaseResponse.get().getPhaseName();
                }
                LocalDateTime time = LocalDateTime.ofEpochSecond(Long.parseLong(timestampValue.toString()), 0, ZoneId.of("Europe/Warsaw").getRules().getOffset(LocalDateTime.now()));
                BigDecimal parsedValue = Convert.fromWei(String.valueOf(amountValue), Convert.Unit.ETHER);

                formattedData.add(new FundsService.TransactionStruct(
                        senderValue.toString(),
                        receiverValue.toString(),
                        phaseName,
                        phaseIdValue.toString(),
                        parsedValue,
                        time.toString()
                ));
        }
            return ResponseEntity.status(HttpStatus.OK).body(formattedData);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    public ResponseEntity<Map<String, String>> sendFundsToOwner(Long phaseId) {
        try {
            Optional<CollectionPhase> collection = collectionPhaseRepository.findPhaseById(phaseId);
            if (collection.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Phase not found!"));
            }

            User user = collectionPhaseService.getCollectionFounder(phaseId).getBody();
            User authUser = userService.getUserFromAuthentication();
            if (!Objects.equals(user.getPublicAddress(), authUser.getPublicAddress())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Only collection owner can withdraw funds!"));
            }

            Funds contract = loadFundsContract();
            TransactionReceipt response = contract.sendFundsToOwner(authUser.getPublicAddress(), BigInteger.valueOf(phaseId)).send();
            if (response.isStatusOK()){
                return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(response.getTransactionHash()));
            }
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new PreparedResponse().getFailureResponse(response.getStatus()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> sendFundsToDonators(Long phaseId) {
        try{
            Optional<CollectionPhase> phase = collectionPhaseRepository.findPhaseById(phaseId);
            if (phase.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Phase not found!"));
            }

            ResponseEntity<List<FundsService.TransactionStruct>> transactions = getTransactionHistory(true);
            if (transactions.getStatusCode() != HttpStatus.OK || (transactions.hasBody() && transactions.getBody().size() == 0)){
                return ResponseEntity.status(transactions.getStatusCode()).body(new PreparedResponse().getFailureResponse("No transactions found in history"));
            }
            User authUser = userService.getUserFromAuthentication();

            List<Long> transactionsIdList = new ArrayList<>();
            for (int i = 0; i < transactions.getBody().size(); i++){
                if (transactions.getBody().get(i).phaseId.equals(phaseId.toString()) && authUser.getPublicAddress().equals(transactions.getBody().get(i).sender)
                        && !Objects.equals(transactions.getBody().get(i).amount, BigDecimal.ZERO)){
                    transactionsIdList.add((long) i);
                }
            }

            if (transactionsIdList.size() == 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("There is no transactions to phase"));
            }

            Funds contract = loadFundsContract();

            List<BigInteger> transactionsList = new ArrayList<>();
            transactionsIdList.forEach(id -> {
                transactionsList.add(BigInteger.valueOf(id));
            });

            TransactionReceipt response = contract.sendFundsToDonators(authUser.getPublicAddress(), BigInteger.valueOf(phaseId), transactionsList).send();
            if (response.isStatusOK()){
                return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(response.getTransactionHash()));
            }
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new PreparedResponse().getFailureResponse(response.getStatus()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> getDonatedFunds(Long phaseId) {
        try {
            Funds contract = loadFundsContract();
            List<Tuple5<String, BigInteger, Boolean, Boolean, BigInteger>> response = Collections.singletonList(contract.fundsDonated(BigInteger.valueOf(phaseId)).send());
            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(response.toString()));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    private ResponseEntity<Tuple5<String, BigInteger, Boolean, Boolean, BigInteger>> getDonatedFundsState(Long phaseId) {
        try {
            Funds contract = loadFundsContract();
            Tuple5<String, BigInteger, Boolean, Boolean, BigInteger> response = contract.fundsDonated(BigInteger.valueOf(phaseId)).send();
            return ResponseEntity.status(HttpStatus.OK).body(response);
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

    public ResponseEntity<List<CollectionPhase>> getSupportedPhases() {
        User authUser = userService.getUserFromAuthentication();
        List<CollectionPhase> phases = collectionPhaseRepository.findSupportedPhasesByState(authUser.getId(), PollState.NEGATIVE, CollUserType.SUSTAINER);

        return phasesWithNotNullTransactions(phases);
    }

    public ResponseEntity<List<CollectionPhase>> getOwnedPhases() {
        User authUser = userService.getUserFromAuthentication();
        List<CollectionPhase> phases = collectionPhaseRepository.findOwnedPhasesByState(authUser.getId(), PollState.POSITIVE, CollUserType.FOUNDER);

        List<CollectionPhase> response = new ArrayList<>();
        ResponseEntity<List<FundsService.TransactionStruct>> transactionHistory = getTransactionHistory(true);
        if (transactionHistory.getStatusCode() != HttpStatus.OK || (transactionHistory.hasBody() && transactionHistory.getBody().size() == 0)){
            return ResponseEntity.status(transactionHistory.getStatusCode()).body(response);
        }

        phases.forEach(phase -> {
            ResponseEntity<Tuple5<String, BigInteger, Boolean, Boolean, BigInteger>> fundsState = getDonatedFundsState(phase.getId());
            if (fundsState.getStatusCode() == HttpStatus.OK){
                if (!Objects.equals(fundsState.getBody().component2(), BigInteger.ZERO) && !Objects.equals(fundsState.getBody().component5(), BigInteger.ZERO)
                        && Objects.equals(fundsState.getBody().component1(), authUser.getPublicAddress())){
                    response.add(phase);
                }
            }
        });

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private ResponseEntity<List<CollectionPhase>> phasesWithNotNullTransactions(List<CollectionPhase> phases){
        List<CollectionPhase> response = new ArrayList<>();
        ResponseEntity<List<FundsService.TransactionStruct>> transactionHistory = getTransactionHistory(false);

        if (transactionHistory.getStatusCode() != HttpStatus.OK || (transactionHistory.hasBody() && transactionHistory.getBody().size() == 0)){
            return ResponseEntity.status(transactionHistory.getStatusCode()).body(response);
        }

        phases.forEach(phase -> {
            AtomicBoolean addToResponse = new AtomicBoolean(false);
            transactionHistory.getBody().forEach(transaction -> {
                if (phase.getId().toString().equals(transaction.phaseId) && !Objects.equals(transaction.amount, BigDecimal.ZERO)){
                    addToResponse.set(true);
                }
            });
            if (addToResponse.get()){
                response.add(phase);
            }
        });

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
