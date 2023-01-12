package com.crowdfunding.crowdfundingapi.poll;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.collection.CollectionRepository;
import com.crowdfunding.crowdfundingapi.collection.CollectionService;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhase;
import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import com.crowdfunding.crowdfundingapi.poll.vote.Vote;
import com.crowdfunding.crowdfundingapi.poll.vote.VoteResult;
import com.crowdfunding.crowdfundingapi.support.CollUserType;
import com.crowdfunding.crowdfundingapi.user.User;
import com.crowdfunding.crowdfundingapi.user.UserService;
import com.crowdfunding.crowdfundingapi.web3.funds.FundsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
public class PollService {

    private final PollRepository repository;
    private final CollectionService collectionService;
    private final FundsService fundsService;
    private final UserService userService;
    private final CollectionRepository collectionRepository;

    public void setAllowedUsersCount(CollectionPhase collectionPhase){
      List<User> supportingUsers = repository.findPhaseSupporters(collectionPhase.getId(), CollUserType.SUSTAINER);
      int count = supportingUsers.size();
      Poll poll = collectionPhase.getPoll();
      if (poll.getAllowedUsersCount() != count){
          poll.setAllowedUsersCount(count);
          repository.save(poll);
      }
    }

    public void setPollResult(Poll poll){
        setAllowedUsersCount(poll.getCollectionPhase());
        List<Vote> votes = poll.getVotes();
        if (votes.size() == poll.getAllowedUsersCount() && poll.getEndDate().isBefore(LocalDateTime.now())){
            AtomicInteger acceptedVotesCount = new AtomicInteger();
            AtomicInteger declinedVotesCount = new AtomicInteger();
            votes.forEach(vote -> {
                if (vote.getVoteResult() == VoteResult.ACCEPTED){
                    acceptedVotesCount.getAndIncrement();
                }else {
                    declinedVotesCount.getAndIncrement();
                }
            });

            if (acceptedVotesCount.get() > declinedVotesCount.get()){
                poll.setState(PollState.POSITIVE);
                fundsService.setCollectionPollEnd(poll.getCollectionPhase().getId());
            }
            else {
                poll.setState(PollState.NEGATIVE);
                fundsService.setCollectionPollEnd(poll.getCollectionPhase().getId());
                fundsService.setCollectionFraud(poll.getCollectionPhase().getId());
            }

            repository.save(poll);
        }
    }
    public ResponseEntity<Map<String, String>> getPollResult(Long phaseId){
        Optional<Poll> optionalPoll = repository.findPollByPhaseId(phaseId);
        if (optionalPoll.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(optionalPoll.get().getState().toString()));
    }

    public ResponseEntity<List<Poll>> getUsersAccessiblePolls( ) {
        User authUser = userService.getUserFromAuthentication();
        List<Poll> polls = repository.findAccessibleByUserPolls(authUser.getId(), CollUserType.SUSTAINER);
        List<Poll> response = new ArrayList<>();
        List<Poll> toDelete = new ArrayList<>();
        polls.forEach(poll -> {
            if (poll.getStartDate().isBefore(LocalDateTime.now())){
                poll.setState(PollState.IN_PROCESS);
                repository.save(poll);
            }

            if (poll.getState() == PollState.IN_PROCESS){
                response.add(poll);
                poll.getVotes().forEach(vote -> {
                    if (vote.getUser() == authUser){
                        toDelete.add(poll);
                    }
                });
            }
        });

        toDelete.forEach(response::remove);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<List<Poll>> getOwnedPolls(Long collectionId) {
        User authUser = userService.getUserFromAuthentication();
        Optional<Collection> collection = collectionRepository.findCollectionById(collectionId);
        if (collection.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Poll> polls = repository.findOwnedPolls(authUser.getId(), CollUserType.FOUNDER, collectionId);
        return ResponseEntity.status(HttpStatus.OK).body(polls);
    }

    public ResponseEntity<List<Poll>> getPollsHistory( ) {
        User authUser = userService.getUserFromAuthentication();
        List<Poll> polls = repository.findPollsHistory(authUser.getId(), CollUserType.SUSTAINER);

        return ResponseEntity.status(HttpStatus.OK).body(polls);
    }


}
