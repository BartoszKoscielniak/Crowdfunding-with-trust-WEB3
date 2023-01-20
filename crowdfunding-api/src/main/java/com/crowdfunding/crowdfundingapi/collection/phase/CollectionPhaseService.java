package com.crowdfunding.crowdfundingapi.collection.phase;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.collection.CollectionRepository;
import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import com.crowdfunding.crowdfundingapi.poll.Poll;
import com.crowdfunding.crowdfundingapi.poll.PollRepository;
import com.crowdfunding.crowdfundingapi.poll.PollState;
import com.crowdfunding.crowdfundingapi.support.CollUserRelation;
import com.crowdfunding.crowdfundingapi.support.CollUserType;
import com.crowdfunding.crowdfundingapi.user.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class CollectionPhaseService {

    private final CollectionPhaseRepository repository;
    private final CollectionRepository collectionRepository;
    private final PollRepository pollRepository;

    public ResponseEntity<CollectionPhase> getPhase(Long id) {
        Optional<CollectionPhase> optionalCollection = repository.findPhaseById(id);

        return optionalCollection.map(collection ->
                ResponseEntity.status(HttpStatus.OK).body(collection)).orElseGet(( ) ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public ResponseEntity<List<CollectionPhase>> getCollectionPhases(Long id) {
        List<CollectionPhase> optionalCollection = repository.findPhaseByCollectionId(id);
        if (optionalCollection.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalCollection);
    }

    public ResponseEntity<Map<String, String>> addPhase(Double goal, String name, String description, String deadline, Long collectionId, LocalDateTime till, String poe) {
        Optional<Collection> optionalCollection = collectionRepository.findCollectionById(collectionId);
        if (optionalCollection.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Collection with provided ID not found"));
        }
        Collection collection = optionalCollection.get();
        CollectionPhase newPhase = new CollectionPhase(goal, name, description, collection, till, poe);
        Poll poll = new Poll(PollState.NOT_ACTIVATED);
        repository.save(newPhase);

        poll.setCollectionPhase(newPhase);
        poll.setStartDate(LocalDateTime.parse(deadline));
        pollRepository.save(poll);

        return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Phase added"));
    }

    public ResponseEntity<Map<String, String>> updatePhase(Long id, String baseDescription) {
        Optional<CollectionPhase> optionalPhase= repository.findPhaseById(id);
        if (optionalPhase.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Phase with provided ID not found"));
        }
        CollectionPhase collectionPhase = optionalPhase.get();

        if (baseDescription != null){
            collectionPhase.setDescription(baseDescription);
        }
        repository.save(collectionPhase);
        return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Phase updated"));
    }

    public ResponseEntity<Map<String, String>> deletePhase(Long id) {
        Optional<CollectionPhase> optionalPhase= repository.findPhaseById(id);
        if (optionalPhase.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Phase with provided ID not found"));
        }
        CollectionPhase collectionPhase = optionalPhase.get();

        repository.delete(collectionPhase);
        return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Phase deleted"));
    }

    public ResponseEntity<User> getCollectionFounder(Long phaseId){
        Optional<CollectionPhase> phase = repository.findPhaseById(phaseId);
        if (phase.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<CollUserRelation> relations = phase.get().getCollection().getCollUserRelations();
        for (CollUserRelation collUserRelation : relations) {
            if (collUserRelation.getType() == CollUserType.FOUNDER) {
                return ResponseEntity.status(HttpStatus.OK).body(collUserRelation.getUser());
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
