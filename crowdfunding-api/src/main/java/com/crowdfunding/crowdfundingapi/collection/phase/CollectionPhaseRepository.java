package com.crowdfunding.crowdfundingapi.collection.phase;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CollectionPhaseRepository extends JpaRepository<CollectionPhase, Long> {

    @Query("SELECT c FROM CollectionPhase c WHERE c.id = ?1")
    Optional<CollectionPhase> findPhaseById(Long id);

    @Query("SELECT c FROM CollectionPhase c WHERE c.collection.id = ?1")
    List<CollectionPhase> findPhaseByCollectionId(Long id);
}
