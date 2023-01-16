package com.crowdfunding.crowdfundingapi.web3.funds;

import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/web3/fund")
@AllArgsConstructor
public class FundsController {

    private final FundsService service;

    @PostMapping(path = "/depositfunds")
    public ResponseEntity<Map<String, String>> depositFunds(
            @RequestParam Long phaseId,
            @RequestParam Double amount) {
        return service.depositFunds(phaseId, amount);
    }

    @PostMapping(path = "/sendfundstoowner")
    public ResponseEntity<Map<String, String>> sendFundsToOwner(
            @RequestParam Long phaseId) {
        return service.sendFundsToOwner(phaseId);
    }

    @PostMapping(path = "/sendfundstodonators")
    public ResponseEntity<Map<String, String>> sendFundsToDonators(
            @RequestParam Long phaseId) {
        return service.sendFundsToDonators(phaseId);
    }

    @PutMapping(path = "/collectionfraud")
    public ResponseEntity<Map<String, String>> setCollectionFraud(
            @RequestParam Long phaseId) {
        return service.setPollFraud(phaseId);
    }

    @PutMapping(path = "/collectionpollend")
    public ResponseEntity<Map<String, String>> setCollectionPollEnd(
            @RequestParam Long phaseId) {
        return service.setPollEnd(phaseId);
    }
    @GetMapping(path = "/collectionfraud")
    public ResponseEntity<Map<String, String>> isCollectionFraud(
            @RequestParam Long phaseId) {
        return service.isFraud(phaseId);
    }

    @GetMapping(path = "/collectionpollend")
    public ResponseEntity<Map<String, String>> isCollectionPollEnd(
            @RequestParam Long phaseId) {
        return service.isPollEnded(phaseId);
    }

    @GetMapping(path = "/transactionhistory")
    public ResponseEntity<List<FundsService.TransactionStruct>> getTransactionHistory() {
        return service.getTransactionHistory(false);
    }

    @GetMapping(path = "/collectionfunds")
    public ResponseEntity<Map<String, String>> getDonatedFunds(
            @RequestParam Long phaseId) {
        return service.getDonatedFunds(phaseId);
    }

    @GetMapping(path = "/availabletoreceive/sustainer")
    public ResponseEntity<List<CollectionPhase>> getSupportedPhasesByPollState(){
        return service.getSupportedPhases();
    }

    @GetMapping(path = "/availabletoreceive/founder")
    public ResponseEntity<List<CollectionPhase>> getOwnedPhasesByPollState(){
        return service.getOwnedPhases();
    }
}
