package com.crowdfunding.crowdfundingapi.collection;

import com.crowdfunding.crowdfundingapi.support.CollUserType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/collection")
@AllArgsConstructor
public class CollectionController {

    private final CollectionService service;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Collection> getCollections(@PathVariable Long id){
        return service.getCollections(id);
    }

    @GetMapping()
    public ResponseEntity<List<Collection>> getAllCollections(
            @RequestParam(required = false) CollectionType type
    ){
        return service.getAllCollections(type);
    }

    @GetMapping(path = "/type")
    public ResponseEntity<List<Collection>> getAllCollectionsByType(
            @RequestParam CollUserType type
    ){
        return service.getAllCollectionsByType(type);
    }

    @PostMapping
    public ResponseEntity<Collection> addCollection(
            @RequestParam Double goal,
            @RequestParam String description,
            @RequestParam CollectionType type
    ){
        return service.addCollection(goal, description, type);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Collection> updateCollection(
            @PathVariable Long id,
            @RequestParam(required = false) CollectionType collectionType,
            @RequestParam(required = false) String baseDescription
    ){
        return service.updateCollection(id, collectionType, baseDescription);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Collection> deleteCollection(
            @PathVariable Long id
    ){
        return service.deleteCollection(id);
    }
}
