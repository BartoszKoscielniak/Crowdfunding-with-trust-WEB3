package com.crowdfunding.crowdfundingapi.collection;

import com.crowdfunding.crowdfundingapi.support.CollUserRelation;
import com.crowdfunding.crowdfundingapi.support.CollUserType;
import com.crowdfunding.crowdfundingapi.support.RelationRepository;
import com.crowdfunding.crowdfundingapi.user.User;
import com.crowdfunding.crowdfundingapi.user.UserRepository;
import com.crowdfunding.crowdfundingapi.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
public class CollectionService {

    private final CollectionRepository repository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RelationRepository relationRepository;

    public ResponseEntity<Collection> getCollection(Long id ) {
        Optional<Collection> optionalCollection = repository.findCollectionById(id);

        return optionalCollection.map(collection ->
                ResponseEntity.status(HttpStatus.OK).body(collection)).orElseGet(( ) ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public ResponseEntity<List<Collection>> getAllCollections(CollectionType type) {
        List<Collection> optionalCollection = repository.findAll();
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

    public ResponseEntity<Collection> addCollection(Double goal, String description, CollectionType type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByPublicAddress(authentication.getName()).get();
        Collection collection = new Collection(goal, description, type, new CollUserRelation(user, CollUserType.FOUNDER));
        repository.save(collection);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<Collection> updateCollection(Long id, CollectionType collectionType, String baseDescription) {
        Optional<Collection> optionalCollection = repository.findCollectionById(id);
        if (optionalCollection.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Collection collection = optionalCollection.get();

        if (collectionType != null){
            collection.setCollectionType(collectionType);
        }

        if (baseDescription != null){
            collection.setBaseDescription(baseDescription);
        }

        repository.save(collection);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<Collection> deleteCollection(Long id) {
        Optional<Collection> optionalCollection = repository.findCollectionById(id);
        if (optionalCollection.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Collection collection = optionalCollection.get();

        List<User> collectionSustainers = getCollectionAssociatedUsers(collection, CollUserType.SUSTAINER);
        if (!collectionSustainers.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        repository.delete(collection);
        return ResponseEntity.status(HttpStatus.OK).build();
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
        Set<CollUserRelation> collUserRelationsSet = collection.getCollUserRelations();
        collUserRelationsSet.stream().forEach(collUserRelation -> {
            if (collUserRelation.getType() == type){
                userSet.add(collUserRelation.getUser());
            }
        });
        return userSet;
    }
}
