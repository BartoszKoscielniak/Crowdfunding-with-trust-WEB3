package com.crowdfunding.crowdfundingapi.poll.vote;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vote")
@AllArgsConstructor
public class VoteController {

    private final VoteService service;

    @PostMapping
    public ResponseEntity addVote(
            @RequestParam Long phaseId,
            @RequestParam VoteResult result
    ){
        return service.addVote(phaseId, result);
    }//TODO:wyniki dla danego glosowania, sprawdz czy glosowanie jest zakonczone, ilosc uprawionych do glosowania


}
