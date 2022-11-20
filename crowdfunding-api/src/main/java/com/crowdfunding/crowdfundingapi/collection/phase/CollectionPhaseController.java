package com.crowdfunding.crowdfundingapi.collection.phase;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<CollectionPhase> addPhase(
            @RequestParam Double goal,
            @RequestParam String description,
            @RequestParam String deadline,
            @RequestParam Long collectionId
    ){
        return service.addPhase(goal, description, deadline, collectionId);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CollectionPhase> updatePhase(
            @PathVariable Long id,
            @RequestParam(required = false) String baseDescription
    ){
        return service.updatePhase(id, baseDescription);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Collection> deletePhase(
            @PathVariable Long id
    ){
        return service.deletePhase(id);
    }
}
