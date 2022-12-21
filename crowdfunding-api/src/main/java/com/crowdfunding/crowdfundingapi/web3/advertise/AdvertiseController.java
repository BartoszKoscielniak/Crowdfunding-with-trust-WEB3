package com.crowdfunding.crowdfundingapi.web3.advertise;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.tuples.generated.Tuple3;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/web3/advertise")
@AllArgsConstructor
public class AdvertiseController {

    private final AdvertiseService service;

    @PostMapping(path = "/buy")
    public ResponseEntity<Map<String, String>> buyAdvertise(
            @RequestParam Long collectionId,
            @RequestParam Long advertiseId) {
        return service.buyAdvertise(collectionId, advertiseId);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Map<String, String>> addAdvertise(
            @RequestParam String name,
            @RequestParam Double price,
            @RequestParam int durationInDays) {
        return service.addType(name, price, durationInDays);
    }

    @GetMapping(path = "/types")
    public ResponseEntity<List<Tuple3<String, BigInteger, BigInteger>>> getTypes() {
        return service.getAdvertiseTypes();
    }
}
