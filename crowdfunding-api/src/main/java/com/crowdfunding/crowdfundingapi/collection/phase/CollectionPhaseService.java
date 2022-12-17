package com.crowdfunding.crowdfundingapi.collection.phase;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.collection.CollectionRepository;
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

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public ResponseEntity<CollectionPhase> addPhase(Double goal, String description, String deadline, Long collectionId) {
        Optional<Collection> optionalCollection = collectionRepository.findCollectionById(collectionId);
        if (optionalCollection.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Collection collection = optionalCollection.get();
        CollectionPhase newPhase = new CollectionPhase(goal, description, collection);
        Poll poll = new Poll(PollState.NOT_ACTIVATED);
        repository.save(newPhase);

        poll.setCollectionPhase(newPhase);
        poll.setStartDate(LocalDate.parse(deadline));
        pollRepository.save(poll);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<CollectionPhase> updatePhase(Long id, String baseDescription) {
        Optional<CollectionPhase> optionalPhase= repository.findPhaseById(id);
        if (optionalPhase.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CollectionPhase collectionPhase = optionalPhase.get();

        if (baseDescription != null){
            collectionPhase.setDescription(baseDescription);
        }
        repository.save(collectionPhase);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<Collection> deletePhase(Long id) {
        Optional<CollectionPhase> optionalPhase= repository.findPhaseById(id);
        if (optionalPhase.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CollectionPhase collectionPhase = optionalPhase.get();

        repository.delete(collectionPhase);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<User> getCollectionFounder(Long phaseId){
        Optional<CollectionPhase> phase = repository.findPhaseById(phaseId);
        if (phase.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Set<CollUserRelation> relations = phase.get().getCollection().getCollUserRelations();
        List<CollUserRelation> list = relations.stream().toList();
        for (CollUserRelation collUserRelation : list) {
            if (collUserRelation.getType() == CollUserType.FOUNDER) {
                return ResponseEntity.status(HttpStatus.OK).body(collUserRelation.getUser());
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
