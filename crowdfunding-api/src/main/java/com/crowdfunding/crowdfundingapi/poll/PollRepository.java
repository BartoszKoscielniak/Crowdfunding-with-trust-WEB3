package com.crowdfunding.crowdfundingapi.poll;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PollRepository extends JpaRepository<Poll, Long> {

    @Query("SELECT p FROM Poll p WHERE p.collectionPhase.id = ?1")
    Optional<Poll> findPollByPhaseId(Long id);
}
