package com.crowdfunding.crowdfundingapi.poll.vote;

import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhase;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhaseRepository;
import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import com.crowdfunding.crowdfundingapi.poll.PollService;
import com.crowdfunding.crowdfundingapi.poll.PollState;
import com.crowdfunding.crowdfundingapi.support.CollUserRelation;
import com.crowdfunding.crowdfundingapi.support.CollUserType;
import com.crowdfunding.crowdfundingapi.support.RelationRepository;
import com.crowdfunding.crowdfundingapi.user.User;
import com.crowdfunding.crowdfundingapi.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository repository;
    private final CollectionPhaseRepository collectionPhaseRepository;
    private final UserService userService;
    private final RelationRepository relationRepository;
    private final PollService pollService;

    public ResponseEntity<Map<String, String>> addVote(Long phaseId, VoteResult result) {
        User user = userService.getUserFromAuthentication();

        Optional<CollectionPhase> optionalPhase = collectionPhaseRepository.findPhaseById(phaseId);
        if (optionalPhase.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CollectionPhase phase = optionalPhase.get();

        Optional<CollUserRelation> optionalRelation = relationRepository.findRelationByUseridPhaseid(user.getId(), phase.getCollection().getId());
        if (optionalRelation.isEmpty() || optionalRelation.get().getType() != CollUserType.SUSTAINER){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new PreparedResponse().getFailureResponse("User does not support phase."));
        }

        Optional<Vote> checkExistingPolls = repository.checkUserVoteExist(user.getId(), phase.getPoll().getId());
        if (checkExistingPolls.isPresent()){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new PreparedResponse().getFailureResponse("User already voted."));
        }

        if (phase.getPoll().getState() != PollState.IN_PROCESS){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new PreparedResponse().getFailureResponse("You cannot vote."));
        }

        if (phase.getPoll().getStartDate().isAfter(LocalDateTime.now())){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new PreparedResponse().getFailureResponse("Poll did not start yet."));
        }

        if (phase.getPoll().getEndDate().isBefore(LocalDateTime.now())){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new PreparedResponse().getFailureResponse("Poll ended, You cannot vote!"));
        }

        repository.save(new Vote(result, phase.getPoll(), user));
        return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Vote added."));
    }
}
