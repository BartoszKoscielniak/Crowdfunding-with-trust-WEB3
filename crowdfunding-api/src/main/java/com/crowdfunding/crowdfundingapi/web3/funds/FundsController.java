package com.crowdfunding.crowdfundingapi.web3.funds;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/web3/fund")
@AllArgsConstructor
public class FundsController {

    private final FundsService fundsService;

    @PostMapping(path = "/depositfunds")
    public ResponseEntity<Map<String, String>> depositFunds(
            @RequestParam Long phaseId,
            @RequestParam Double amount) {
        return fundsService.depositFunds(phaseId, amount);
    }

    @PostMapping(path = "/sendfundstoowner")
    public ResponseEntity<Map<String, String>> sendFundsToOwner(
            @RequestParam Long phaseId) {
        return fundsService.sendFundsToOwner(phaseId);
    }

    @PostMapping(path = "/sendfundstodonators")
    public ResponseEntity<Map<String, String>> sendFundsToDonators(
            @RequestParam Long phaseId) {
        return fundsService.sendFundsToDonators(phaseId);
    }

    @PutMapping(path = "/collectionfraud")
    public ResponseEntity<Map<String, String>> setCollectionFraud(
            @RequestParam Long phaseId) {
        return fundsService.setCollectionFraud(phaseId);
    }

    @PutMapping(path = "/collectionpollend")
    public ResponseEntity<Map<String, String>> setCollectionPollEnd(
            @RequestParam Long phaseId) {
        return fundsService.setCollectionPollEnd(phaseId);
    }
    @GetMapping(path = "/collectionfraud")
    public ResponseEntity<Map<String, String>> isCollectionFraud(
            @RequestParam Long phaseId) {
        return fundsService.isFraud(phaseId);
    }

    @GetMapping(path = "/collectionpollend")
    public ResponseEntity<Map<String, String>> isCollectionPollEnd(
            @RequestParam Long phaseId) {
        return fundsService.isPollEnded(phaseId);
    }

    @GetMapping(path = "/transactionhistory")
    public ResponseEntity<List<FundsService.TransactionStruct>> getTransactionHistory() {
        return fundsService.getTransactionHistory(false);
    }

    @GetMapping(path = "/collectionfunds")
    public ResponseEntity<Map<String, String>> getDonatedFunds(
            @RequestParam Long phaseId) {
        return fundsService.getDonatedFunds(phaseId);
    }
}
