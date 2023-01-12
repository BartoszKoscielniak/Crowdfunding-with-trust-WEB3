package com.crowdfunding.crowdfundingapi.poll;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/poll")
@AllArgsConstructor
public class PollController {

    private final PollService pollService;

    @GetMapping(path = "/result/{phaseid}")
    public ResponseEntity<Map<String, String>> getPollResult(@PathVariable Long phaseid ){
        return pollService.getPollResult(phaseid);
    }

    @GetMapping(path = "/usersaccessible")
    public ResponseEntity<List<Poll>> getUsersAccessiblePolls(){
        return pollService.getUsersAccessiblePolls();
    }

    @GetMapping(path = "/collectionpolls")
    public ResponseEntity<List<Poll>> getOwnedPolls(@RequestParam Long collectionid){
        return pollService.getOwnedPolls(collectionid);
    }

    @GetMapping(path = "/pollshistory")
    public ResponseEntity<List<Poll>> getPollsHistory(){
        return pollService.getPollsHistory();
    }
}
