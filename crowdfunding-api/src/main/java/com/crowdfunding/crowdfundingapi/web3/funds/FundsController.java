package com.crowdfunding.crowdfundingapi.web3.funds;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Type;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/web3/fund")
@AllArgsConstructor
public class FundsController {

    private final FundsService fundsService;

    @PostMapping(path = "/depositfunds")
    public ResponseEntity<Map<String, String>> depositFunds(
            @RequestParam Long collectionId,
            @RequestParam Double amount) {
        return fundsService.depositFunds(collectionId, amount);
    }

    @PostMapping(path = "/sendfundstoowner")
    public ResponseEntity<Map<String, String>> sendFundsToOwner(
            @RequestParam Long collectionId) {
        return fundsService.sendFundsToOwner(collectionId);
    }

    @PostMapping(path = "/sendfundstodonators")
    public ResponseEntity<Map<String, String>> sendFundsToDonators(
            @RequestParam Long collectionId,
            @RequestParam List<Long> transactionIdList) {
        return fundsService.sendFundsToDonators(collectionId, transactionIdList);
    }

    @PutMapping(path = "/collectionfraud")
    public ResponseEntity<Map<String, String>> setCollectionFraud(
            @RequestParam Long collectionId) {
        return fundsService.setCollectionFraud(collectionId);
    }

    @PutMapping(path = "/collectionpollend")
    public ResponseEntity<Map<String, String>> setCollectionPollEnd(
            @RequestParam Long collectionId) {
        return fundsService.setCollectionPollEnd(collectionId);
    }
    @GetMapping(path = "/collectionfraud")
    public ResponseEntity<Map<String, String>> isCollectionFraud(
            @RequestParam Long collectionId) {
        return fundsService.isFraud(collectionId);
    }

    @GetMapping(path = "/collectionpollend")
    public ResponseEntity<Map<String, String>> isCollectionPollEnd(
            @RequestParam Long collectionId) {
        return fundsService.isPollEnded(collectionId);
    }

    @GetMapping(path = "/transactionhistory")
    public ResponseEntity<Map<String, String>> getTransactionHistory() {
        return fundsService.getTransactionHistory();
    }

    @GetMapping(path = "/collectionfunds")
    public ResponseEntity<Map<String, String>> getDonatedFunds(
            @RequestParam Long collectionId) {
        return fundsService.getDonatedFunds(collectionId);
    }
}
