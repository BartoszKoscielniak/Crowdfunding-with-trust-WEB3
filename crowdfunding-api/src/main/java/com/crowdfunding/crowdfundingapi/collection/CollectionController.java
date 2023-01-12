package com.crowdfunding.crowdfundingapi.collection;

import com.crowdfunding.crowdfundingapi.support.CollUserType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/collection")
@AllArgsConstructor
public class CollectionController {

    private final CollectionService service;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Collection> getCollections(@PathVariable Long id){
        return service.getCollection(id);
    }

    @GetMapping(path = "/owned")
    public ResponseEntity<List<Collection>> getOwnedCollections(){
        return service.getOwnedCollection();
    }

    @GetMapping()
    public ResponseEntity<List<Collection>> getAllCollections(
            @RequestParam(required = false) CollectionType type,
            @RequestParam(required = false) String name){
        return service.getAllCollections(type, name);
    }

    @GetMapping(path = "/type")
    public ResponseEntity<List<Collection>> getAllCollectionsByType(
            @RequestParam CollUserType type){
        return service.getAllCollectionsByType(type);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addCollection(
            @RequestParam String description,
            @RequestParam String name,
            @RequestParam CollectionType type,
            @RequestParam String phase1name,
            @RequestParam String phase1description,
            @RequestParam String phase1till,
            @RequestParam Double phase1goal,
            @RequestParam(required = false) String phase1proofOfEvidence,
            @RequestParam(required = false) String phase2name,
            @RequestParam(required = false) String phase2description,
            @RequestParam(required = false) String phase2till,
            @RequestParam(required = false) Double phase2goal,
            @RequestParam(required = false) String phase2proofOfEvidence,
            @RequestParam(required = false) String phase3name,
            @RequestParam(required = false) String phase3description,
            @RequestParam(required = false) String phase3till,
            @RequestParam(required = false) Double phase3goal,
            @RequestParam(required = false) String phase3proofOfEvidence,
            @RequestParam(required = false) String phase4name,
            @RequestParam(required = false) String phase4description,
            @RequestParam(required = false) String phase4till,
            @RequestParam(required = false) Double phase4goal,
            @RequestParam(required = false) String phase4proofOfEvidence){
        return service.addCollection(name, description, type,
                phase1name, phase1description, phase1goal,phase1till, phase1proofOfEvidence,
                phase2name, phase2description, phase2goal,phase2till, phase2proofOfEvidence,
                phase3name, phase3description, phase3goal,phase3till, phase3proofOfEvidence,
                phase4name, phase4description, phase4goal,phase4till, phase4proofOfEvidence);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Map<String, String>> updateCollection(
            @PathVariable Long id,
            @RequestParam(required = false) CollectionType collectionType,
            @RequestParam(required = false) String baseDescription){
        return service.updateCollection(id, collectionType, baseDescription);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Map<String, String>> deleteCollection(
            @PathVariable Long id){
        return service.deleteCollection(id);
    }

    @PostMapping(path = "/publish")
    public ResponseEntity<Map<String, String>> publishCollection(
            @RequestParam Long collectionId){
        return service.publishCollection(collectionId);
    }
}
