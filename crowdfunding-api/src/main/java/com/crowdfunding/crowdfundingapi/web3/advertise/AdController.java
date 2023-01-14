package com.crowdfunding.crowdfundingapi.web3.advertise;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/web3/ad")
@AllArgsConstructor
public class AdController {

    private final AdvertiseService service;

    @GetMapping(path = "/types")
    public ResponseEntity<List<AdvertiseService.AdTypeStruct>> getTypes() {
        return service.getAdvertiseTypes();
    }

    @GetMapping(path = "/history")
    public ResponseEntity<List<AdvertiseService.TransactionStruct>> getHistory() {
        return service.getHistory();
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Map<String, String>> addAdvertise(
            @RequestParam String name,
            @RequestParam Double price,
            @RequestParam int durationInDays) {
        return service.addType(name, price, durationInDays);
    }

    @PostMapping(path = "/buy")
    public ResponseEntity<Map<String, String>> buyAdvertise(
            @RequestParam Long collectionId,
            @RequestParam Long advertiseId) {
        return service.buyAdvertise(collectionId, advertiseId);
    }

    @PostMapping(path = "/withdraw")
    public ResponseEntity<Map<String, String>> withdraw() {
        return service.withdrawAdFunds();
    }

    @GetMapping(path = "/balance")
    public ResponseEntity<Map<String, String>> getBalance() {
        return service.getAdvertiseBalance();
    }
}
