package com.crowdfunding.crowdfundingapi.poll.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT v FROM Vote v WHERE v.user.id = ?1 AND v.poll.id = ?2")
    Optional<Vote> checkUserVoteExist(Long userId, Long phaseId);
}
