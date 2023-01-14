package com.crowdfunding.crowdfundingapi.collection.phase;

import com.crowdfunding.crowdfundingapi.poll.PollState;
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

    @GetMapping(path = "/collectionphases/sustainer/{state}")
    public ResponseEntity<List<CollectionPhase>> getSupportedPhasesByPollState(
            @PathVariable PollState state ){
        return service.getSupportedPhases(state);
    }

    @GetMapping(path = "/collectionphases/founder/{state}")
    public ResponseEntity<List<CollectionPhase>> getOwnedPhasesByPollState(
            @PathVariable PollState state ){
        return service.getOwnedPhases(state);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addPhase(
            @RequestParam Double goal,
            @RequestParam String description,
            @RequestParam String name,
            @RequestParam String deadline,
            @RequestParam Long collectionId,
            @RequestParam LocalDateTime till,
            @RequestParam String proofofevidence
            ){
        return service.addPhase(goal, name, description, deadline, collectionId, till, proofofevidence);
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
