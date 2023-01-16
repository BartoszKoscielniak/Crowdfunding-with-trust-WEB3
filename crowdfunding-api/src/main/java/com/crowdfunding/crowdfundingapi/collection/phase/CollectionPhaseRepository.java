package com.crowdfunding.crowdfundingapi.collection.phase;

import com.crowdfunding.crowdfundingapi.poll.PollState;
import com.crowdfunding.crowdfundingapi.support.CollUserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CollectionPhaseRepository extends JpaRepository<CollectionPhase, Long> {

    @Query("SELECT c FROM CollectionPhase c WHERE c.id = ?1")
    Optional<CollectionPhase> findPhaseById(Long id);

    @Query("SELECT c FROM CollectionPhase c WHERE c.collection.id = ?1")
    List<CollectionPhase> findPhaseByCollectionId(Long id);

    @Query("SELECT c FROM CollectionPhase c, Poll p, CollUserRelation r, User u WHERE u.id = ?1 AND p.state = ?2 AND r.type = ?3 AND u.id = r.user.id AND c.id = r.phase.id AND p.collectionPhase.id = c.id")
    List<CollectionPhase> findSupportedPhasesByState(Long userId, PollState state, CollUserType type);

    @Query("SELECT cp FROM CollectionPhase cp, Collection c, Poll p, CollUserRelation r WHERE r.user.id = ?1 AND p.state = ?2 AND r.type = ?3 AND r.collectionRelation.id = c.id AND cp.collection.id = c.id AND p.collectionPhase.id = cp.id")
    List<CollectionPhase> findOwnedPhasesByState(Long id, PollState state, CollUserType type);
}
