package com.crowdfunding.crowdfundingapi.poll;

import com.crowdfunding.crowdfundingapi.collection.CollectionService;
import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import com.crowdfunding.crowdfundingapi.poll.vote.Vote;
import com.crowdfunding.crowdfundingapi.poll.vote.VoteResult;
import com.crowdfunding.crowdfundingapi.support.CollUserType;
import com.crowdfunding.crowdfundingapi.user.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
public class PollService {

    private final PollRepository repository;
    private final CollectionService collectionService;

    public void setAllowedUsersCount(Poll poll){//TODO:dodaj przy tworzeniu transakcji z wspieraniem oraz dodaj uzytkownika do wspierajacych rowniez
      List<User> supportingUsers = collectionService.getCollectionAssociatedUsers(poll.getCollectionPhase().getCollection(), CollUserType.SUSTAINER);
      int count = supportingUsers.size();
      poll.setAllowedUsersCount(count);
      repository.save(poll);
    }

    public void setPollResult(Poll poll){
        Set<Vote> votes = poll.getVotes();
        if (votes.size() == poll.getAllowedUsersCount()){
            AtomicInteger acceptedVotesCount = new AtomicInteger();
            AtomicInteger declinedVotesCount = new AtomicInteger();
            votes.stream().forEach(vote -> {
                if (vote.getVoteResult() == VoteResult.ACCEPTED){
                    acceptedVotesCount.getAndIncrement();
                }else {
                    declinedVotesCount.getAndIncrement();
                }
            });

            if (acceptedVotesCount.get() > declinedVotesCount.get()){
                poll.setState(PollState.POSITIVE);
            }
            else {
                poll.setState(PollState.NEGATIVE);
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
}
