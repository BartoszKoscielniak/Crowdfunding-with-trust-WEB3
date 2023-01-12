package com.crowdfunding.crowdfundingapi.poll;

import com.crowdfunding.crowdfundingapi.support.CollUserType;
import com.crowdfunding.crowdfundingapi.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PollRepository extends JpaRepository<Poll, Long> {

    @Query("SELECT p FROM Poll p WHERE p.collectionPhase.id = ?1")
    Optional<Poll> findPollByPhaseId(Long id);

    @Query("SELECT p FROM Poll p, CollUserRelation r, Collection c, CollectionPhase cp WHERE r.user.id = ?1 AND r.type = ?2 AND r.collectionRelation.id = c.id AND cp.collection.id = c.id AND p.collectionPhase.id = cp.id AND cp.id= r.phase.id")
    List<Poll> findAccessibleByUserPolls(Long userId, CollUserType type);

    @Query("SELECT p FROM Poll p, CollUserRelation r, Collection c, CollectionPhase cp WHERE r.user.id = ?1 AND r.type = ?2 AND c.id = ?3 AND r.collectionRelation.id = c.id AND cp.collection.id = c.id AND p.collectionPhase.id = cp.id")
    List<Poll> findOwnedPolls(Long userId, CollUserType type, Long collectionId);

    @Query("SELECT p FROM Poll p, Vote v WHERE v.user.id = ?1 AND p.id = v.poll.id ")
    List<Poll> findPollsHistory(Long userId, CollUserType type);

    @Query("SELECT u FROM User u, CollUserRelation r WHERE r.phase.id = ?1 AND r.type = ?2 AND u.id = r.user.id AND r.phase.id is not null")
    List<User> findPhaseSupporters(Long phaseId, CollUserType type);
}
