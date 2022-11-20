package com.crowdfunding.crowdfundingapi.collection.phase;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.collection.CollectionRepository;
import com.crowdfunding.crowdfundingapi.poll.Poll;
import com.crowdfunding.crowdfundingapi.poll.PollRepository;
import com.crowdfunding.crowdfundingapi.poll.PollState;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
}
