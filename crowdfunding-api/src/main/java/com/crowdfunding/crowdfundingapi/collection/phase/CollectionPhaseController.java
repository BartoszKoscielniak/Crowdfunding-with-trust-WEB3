package com.crowdfunding.crowdfundingapi.collection.phase;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/phase")
@AllArgsConstructor
public class CollectionPhaseController {

    private final CollectionPhaseService service;

    @GetMapping(path = "/{id}")
    public ResponseEntity<CollectionPhase> getPhase(@PathVariable Long id){
        return service.getPhase(id);
    }

    @GetMapping(path = "/collectionphases/{id}")
    public ResponseEntity<List<CollectionPhase>> getCollectionPhases(@PathVariable Long id){
        return service.getCollectionPhases(id);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addPhase(
            @RequestParam Double goal,
            @RequestParam String description,
            @RequestParam String deadline,
            @RequestParam Long collectionId,
            @RequestParam LocalDateTime till
            ){
        return service.addPhase(goal, description, deadline, collectionId, till);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Map<String, String>> updatePhase(
            @PathVariable Long id,
            @RequestParam(required = false) String baseDescription
    ){
        return service.updatePhase(id, baseDescription);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Map<String, String>> deletePhase(
            @PathVariable Long id
    ){
        return service.deletePhase(id);
    }
}
