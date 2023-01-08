package com.crowdfunding.crowdfundingapi.poll;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/poll")
@AllArgsConstructor
public class PollController {

    private final PollService pollService;

    @GetMapping(path = "/result/{phaseid}")
    public ResponseEntity<Map<String, String>> getPollResult(
            @PathVariable Long phaseid
    ){
        return pollService.getPollResult(phaseid);
    }
}
