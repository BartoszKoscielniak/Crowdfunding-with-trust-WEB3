package com.crowdfunding.crowdfundingapi.collection;

import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhase;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhaseService;
import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import com.crowdfunding.crowdfundingapi.support.CollUserRelation;
import com.crowdfunding.crowdfundingapi.support.CollUserType;
import com.crowdfunding.crowdfundingapi.support.RelationRepository;
import com.crowdfunding.crowdfundingapi.user.User;
import com.crowdfunding.crowdfundingapi.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
@AllArgsConstructor
public class CollectionService {

    private final CollectionRepository repository;
    private final UserService userService;
    private final RelationRepository relationRepository;
    private final CollectionPhaseService phaseService;

    public ResponseEntity<Collection> getCollection(Long id) {
        Optional<Collection> optionalCollection = repository.findCollectionById(id);
        if (optionalCollection.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        checkPromoExpiration(List.of(optionalCollection.get()));

        return ResponseEntity.status(HttpStatus.OK).body(optionalCollection.get());
    }

    public ResponseEntity<List<Collection>> getAllCollections(CollectionType type) {
        List<Collection> optionalCollection = repository.findAll();
        checkPromoExpiration(optionalCollection);
        if (optionalCollection.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (type != null){
            List<Collection> response = new ArrayList<>();
            optionalCollection.stream().forEach(collection -> {
                if (collection.getCollectionType() == type){
                    response.add(collection);
                }
            });
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(optionalCollection);
    }

    private void checkPromoExpiration(List<Collection> collectionsList){
        collectionsList.forEach(collection -> {
            if (collection.getPromoted() && LocalDateTime.now().isAfter(collection.getPromoTo())){
                collection.setPromoted(false);
                repository.save(collection);
            }
        });
    }

    public ResponseEntity<Map<String, String>> addCollection(Double goal, String description, CollectionType type) {
        User user = userService.getUserFromAuthentication();

        if (goal <= 0.5){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Incorrect collection goal"));
        }

        if (description.length() < 650){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Description is too short. Minimum is 650 characters"));
        }

        Collection collection = new Collection(goal, description, type, new CollUserRelation(user, CollUserType.FOUNDER));
        repository.save(collection);

        return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Collection added"));
    }

    public ResponseEntity<Map<String, String>> updateCollection(Long id, CollectionType collectionType, String baseDescription) {
        Optional<Collection> optionalCollection = repository.findCollectionById(id);
        if (optionalCollection.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Collection with provided ID not found"));
        }
        Collection collection = optionalCollection.get();

        List<User> collectionSustainers = getCollectionAssociatedUsers(collection, CollUserType.SUSTAINER);
        if (!collectionSustainers.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new PreparedResponse().getFailureResponse("You cannot update collection. Collection already have sustainers"));
        }

        if (collectionType != null){
            collection.setCollectionType(collectionType);
        }

        if (baseDescription != null){
            collection.setBaseDescription(baseDescription);
        }

        repository.save(collection);
        return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Collection updated"));
    }

    public ResponseEntity<Map<String, String>> deleteCollection(Long id) {
        Optional<Collection> optionalCollection = repository.findCollectionById(id);
        if (optionalCollection.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Collection with provided ID not found"));
        }
        Collection collection = optionalCollection.get();

        List<User> collectionSustainers = getCollectionAssociatedUsers(collection, CollUserType.SUSTAINER);
        if (!collectionSustainers.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new PreparedResponse().getFailureResponse("You cannot delete collection. Collection already have sustainers"));
        }

        repository.delete(collection);
        return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Collection deleted"));
    }

    public ResponseEntity<List<Collection>> getAllCollectionsByType(CollUserType type) {

        List<Collection> response = new ArrayList<>();
        User user = userService.getUserFromAuthentication();
        List<CollUserRelation> optionalRelation = relationRepository.findRelationByUserid(user.getId());
        if (optionalRelation.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        optionalRelation.stream().forEach(collUserRelation -> {
            if (collUserRelation.getType() == type){
                response.add(collUserRelation.getCollectionRelation());
            }
        });

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public List<User> getCollectionAssociatedUsers(Collection collection, CollUserType type){
        List<User> userSet = new ArrayList<>();
        List<CollUserRelation> collUserRelationsSet = collection.getCollUserRelations();
        collUserRelationsSet.forEach(collUserRelation -> {
            if (collUserRelation.getType() == type){
                userSet.add(collUserRelation.getUser());
            }
        });
        return userSet;
    }

    public ResponseEntity<Map<String, String>> publishCollection(Long collectionId) {
        Optional<Collection> optionalCollection = repository.findCollectionById(collectionId);
        if (optionalCollection.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Collection not found"));
        }

        if (!userService.getUserFromAuthentication().getPublicAddress().equals(
                phaseService.getCollectionFounder(optionalCollection.get().getCollectionPhase().get(0).getId()).getBody().getPublicAddress())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Only collection owner can publish it"));
        }

        Collection collection = optionalCollection.get();
        collection.setState(State.PUBLISHED);
        repository.save(collection);
        return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Collection published"));
    }
}
