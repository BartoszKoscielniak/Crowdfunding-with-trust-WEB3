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
    public ResponseEntity<List<CollectionPhase>> getPhase(@PathVariable Long id){
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
            @RequestParam String name,
            @RequestParam String deadline,
            @RequestParam Long collectionId,
            @RequestParam String proofofevidence
            ){
        return service.addPhase(goal, name, description, deadline, collectionId, proofofevidence);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Map<String, String>> updatePhase(
            @PathVariable Long id,
            @RequestParam String basedescription
    ){
        return service.updatePhase(id, basedescription);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Map<String, String>> deletePhase(
            @PathVariable Long id
    ){
        return service.deletePhase(id);
    }
}
