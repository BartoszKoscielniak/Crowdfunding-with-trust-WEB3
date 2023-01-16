package com.crowdfunding.crowdfundingapi.collection;

import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhase;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhaseRepository;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhaseService;
import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import com.crowdfunding.crowdfundingapi.support.CollUserRelation;
import com.crowdfunding.crowdfundingapi.support.CollUserType;
import com.crowdfunding.crowdfundingapi.support.RelationRepository;
import com.crowdfunding.crowdfundingapi.user.User;
import com.crowdfunding.crowdfundingapi.user.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@AllArgsConstructor
public class CollectionService {

    private final CollectionRepository repository;
    private final CollectionPhaseRepository phaseRepository;
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

    public ResponseEntity<List<Collection>> getAllCollections(CollectionType type, String name, Boolean excludeRequester) {
        List<Collection> optionalCollection = repository.findAllByPromo();
        checkPromoExpiration(optionalCollection);

        List<Collection> publishedCollections = new ArrayList<>();
        for (int i = 0; i < optionalCollection.size(); i++){
            if (optionalCollection.get(i).getState() == State.PUBLISHED){
                publishedCollections.add(optionalCollection.get(i));
            }
        }

        if (publishedCollections.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(publishedCollections);
        }

        User authUser = userService.getUserFromAuthentication();
        List<Collection> notOwned = new ArrayList<>();
        if (excludeRequester != null && excludeRequester){
            for (int i = 0; i < publishedCollections.size(); i++){
                if (!Objects.equals(phaseService.getCollectionFounder(publishedCollections.get(i).getCollectionPhase().get(0).getId()).getBody().getPublicAddress(), authUser.getPublicAddress())){
                    notOwned.add(publishedCollections.get(i));
                }
            }
            publishedCollections = notOwned;
        }

        List<Collection> response = new ArrayList<>();
        if (type != null && name == null){
            publishedCollections.stream().forEach(collection -> {
                if (collection.getCollectionType() == type){
                    response.add(collection);
                }
            });
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        if (name != null && type == null){
            publishedCollections.stream().forEach(collection -> {
                if (collection.getCollectionName().toLowerCase().contains(name)){
                    response.add(collection);
                }
            });
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        if (name != null && type != null){
            publishedCollections.stream().forEach(collection -> {
                if (collection.getCollectionName().toLowerCase().contains(name) && collection.getCollectionType() == type){
                    response.add(collection);
                }
            });
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(publishedCollections);
    }

    private void checkPromoExpiration(List<Collection> collectionsList){
        collectionsList.forEach(collection -> {
            if (collection.getPromoted() && LocalDateTime.now().isAfter(collection.getPromoTo())){
                collection.setPromoted(false);
                repository.save(collection);
            }
        });
    }

    public ResponseEntity<Map<String, String>> addCollection(String name, String description, CollectionType type,
                                 String phase1name, String phase1description, Double phase1goal, String phase1till, String phase1poe,
                                 String phase2name, String phase2description, Double phase2goal, String phase2till, String phase2poe,
                                 String phase3name, String phase3description, Double phase3goal, String phase3till, String phase3poe,
                                 String phase4name, String phase4description, Double phase4goal, String phase4till, String phase4poe) {
        try {
            User user = userService.getUserFromAuthentication();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            Optional<Collection> collectionsWithName = repository.findCollectionByName(name);

            if (collectionsWithName.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Collection with provided name already exist!"));
            }

            if (description.length() < 100 || description.length() > 255){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Description should have at least 100-255 characters!"));
            }

            if (name.length() < 10 || name.length() > 45){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Name should have at least 10-45 characters!"));
            }

            double collectionGoal = 0.0;
            phase2goal = phase2name.length() != 0 ? phase2goal : 0;
            phase3goal = phase3name.length() != 0 ? phase3goal : 0;
            phase4goal = phase4name.length() != 0 ? phase4goal : 0;
            collectionGoal = phase1goal + phase2goal + phase3goal + phase4goal;

            if (phase1description.length() < 100 || phase1description.length() > 255){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Phase description should have at least 100-255 characters!"));
            }

            if (phase1name.length() < 10 || phase1name.length() > 45){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Phase name should have at least 10-45 characters!"));
            }

            if (phase1till.length() == 0 ){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Date not provided!"));
            }

            if (!LocalDateTime.parse(phase1till + " 23:59", formatter).isAfter(LocalDateTime.now())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Cannot use date from past"));
            }

            if (phase1goal < 0.5){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Incorrect collection goal. Minimum is 0.5 ETH per phase."));
            }

            UrlValidator validator = new UrlValidator();
            if (type == CollectionType.STARTUP && !validator.isValid(phase1poe)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("URL is not valid"));
            }

            Collection collection = new Collection(name, collectionGoal, description, type, new CollUserRelation(user, CollUserType.FOUNDER));
            Optional<CollectionPhase> optionalPhase2 = Optional.empty();
            Optional<CollectionPhase> optionalPhase3 = Optional.empty();
            Optional<CollectionPhase> optionalPhase4 = Optional.empty();

            if (phase2name.length() != 0 && phase2goal != 0 && phase2description.length() != 0 && phase2till.length() != 0 && phase2poe.length() != 0){
                ResponseEntity<Map<String, String>> validateResult = validatePhase(phase2name, phase2description, phase2goal, phase2till, phase1till, formatter, phase2poe);
                if (validateResult.getStatusCode() == HttpStatus.BAD_REQUEST){
                    return validateResult;
                }
                optionalPhase2 = Optional.of(new CollectionPhase(phase2goal, phase2description, phase2name, collection, LocalDateTime.parse(phase2till + " 23:59", formatter), phase2poe));
            }

            if (phase3name.length() != 0 && phase3goal != 0 && phase3description.length() != 0 && phase3till.length() != 0 && phase3poe.length() != 0){
                ResponseEntity<Map<String, String>> validateResult = validatePhase(phase3name, phase3description, phase3goal, phase3till, phase2till, formatter, phase3poe);
                if (validateResult.getStatusCode() == HttpStatus.BAD_REQUEST){
                    return validateResult;
                }
                optionalPhase3 = Optional.of(new CollectionPhase(phase3goal, phase3description, phase3name, collection, LocalDateTime.parse(phase3till + " 23:59", formatter), phase3poe));
            }

            if (phase4name.length() != 0 && phase4goal != 0 && phase4description.length() != 0 && phase4till.length() != 0 && phase4poe.length() != 0){
                ResponseEntity<Map<String, String>> validateResult = validatePhase(phase4name, phase4description, phase4goal, phase4till, phase3till, formatter, phase4poe);
                if (validateResult.getStatusCode() == HttpStatus.BAD_REQUEST){
                    return validateResult;
                }
                optionalPhase4 = Optional.of(new CollectionPhase(phase4goal, phase4description, phase4name, collection, LocalDateTime.parse(phase4till + " 23:59", formatter), phase4poe));
            }

            repository.save(collection);
            phaseRepository.save(new CollectionPhase(phase1goal, phase1description, phase1name, collection, LocalDateTime.parse(phase1till + " 23:59", formatter), phase1poe));
            optionalPhase2.ifPresent(phaseRepository::save);
            optionalPhase3.ifPresent(phaseRepository::save);
            optionalPhase4.ifPresent(phaseRepository::save);

            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Collection added"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> validatePhase(String phaseName, String phaseDescription, Double goal, String till, String previousPhaseDeadline, DateTimeFormatter formatter, String poe){
        if (phaseDescription.length() < 100 || phaseDescription.length() > 255){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Phase description should have at least 100-255 characters!"));
        }

        if (phaseName.length() < 10 || phaseName.length() > 45){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Phase name should have at least 10-45 characters!"));
        }

        if (goal < 0.5){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Incorrect collection goal. Minimum is 0.5 ETH per phase."));
        }

        if (!LocalDateTime.parse(till + " 23:59", formatter).isAfter(LocalDateTime.parse(previousPhaseDeadline + " 23:59", formatter))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Each phase deadline have to be after previous!"));
        }

        UrlValidator validator = new UrlValidator();
        if (!validator.isValid(poe)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("URL is not valid"));
        }

        return ResponseEntity.status(HttpStatus.OK).build();
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Collection not found"));
        }
        Collection collection = optionalCollection.get();

        User authUser = userService.getUserFromAuthentication();
        User user = phaseService.getCollectionFounder(collection.getCollectionPhase().get(0).getId()).getBody();
        if (!Objects.equals(user.getPublicAddress(), authUser.getPublicAddress())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("You are not owner!"));
        }

        List<User> collectionSustainers = getCollectionAssociatedUsers(collection, CollUserType.SUSTAINER);
        if (!collectionSustainers.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Cannot delete collection. Collection already have sustainers"));
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

    public ResponseEntity<List<Collection>> getOwnedCollection( ) {
        User authUser = userService.getUserFromAuthentication();
        List<Collection> ownedCollection = relationRepository.findCollectionsByRelation(CollUserType.FOUNDER, authUser.getPublicAddress());
        return ResponseEntity.status(HttpStatus.OK).body(ownedCollection);
    }
}
