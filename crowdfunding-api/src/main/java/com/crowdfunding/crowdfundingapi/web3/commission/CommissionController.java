package com.crowdfunding.crowdfundingapi.web3.commission;

import com.crowdfunding.crowdfundingapi.collection.CollectionType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/web3/commission")
@AllArgsConstructor
public class CommissionController {

    private CommissionService service;

    @GetMapping(path = "/balance")
    public ResponseEntity<Map<String, String>> getBalance() {
        return service.getCommissionBalance();
    }

    @PostMapping(path = "/withdraw")
    public ResponseEntity<Map<String, String>> withdraw() {
        return service.withdrawCommission();
    }

    @PostMapping(path = "/pay")
    public ResponseEntity<Map<String, String>> pay(
            @RequestParam Double amount,
            @RequestParam CollectionType type) {
        return service.payCommission(amount, type);
    }

    @GetMapping(path = "/amount")
    public ResponseEntity<Map<String, String>> getCommissionAmount(
            @RequestParam Double amount,
            @RequestParam CollectionType type) {
        return service.getCommissionAmount(amount, type);
    }
}
