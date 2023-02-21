package com.crowdfunding.crowdfundingapi.poll;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.collection.CollectionRepository;
import com.crowdfunding.crowdfundingapi.poll.vote.Vote;
import com.crowdfunding.crowdfundingapi.poll.vote.VoteResult;
import com.crowdfunding.crowdfundingapi.support.CollUserType;
import com.crowdfunding.crowdfundingapi.user.User;
import com.crowdfunding.crowdfundingapi.user.UserService;
import com.crowdfunding.crowdfundingapi.web3.funds.FundsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PollControllerTest {

    @Autowired
    MockMvc mock;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private PollRepository repository;
    @MockBean
    private FundsService fundsService;
    @MockBean
    private UserService userService;
    @MockBean
    private CollectionRepository collectionRepository;
    @InjectMocks
    private PollService pollService;

    private final User user = User.builder()
            .id(1L)
            .publicAddress("123")
            .build();

    private final User voter = User.builder()
            .id(1L)
            .publicAddress("123")
            .build();

    private final Poll poll = Poll.builder()
            .id(1L)
            .startDate(LocalDateTime.now().minusDays(1))
            .endDate(LocalDateTime.now().plusDays(1))
            .state(PollState.IN_PROCESS)
            .build();

    private final Vote vote = Vote.builder()
            .voteResult(VoteResult.ACCEPTED)
            .id(1L)
            .poll(poll)
            .user(voter)
            .build();

    private final Collection collection = Collection.builder()
            .id(1L)
            .build();

    @Test
    void getPollResult( ) throws Exception {
        when(repository.findPollByPhaseId(1L)).thenReturn(Optional.of(poll));

        mock.perform(get("/api/poll/result/{phaseid}", 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is("IN_PROCESS")))
                .andExpect(jsonPath("$.status", is("success")));
    }

    @Test
    void getUsersAccessiblePolls( ) throws Exception {
        when(userService.getUserFromAuthentication()).thenReturn(user);
        poll.setVotes(List.of(vote));
        when(repository.findAccessibleByUserPolls(user.getId(), CollUserType.SUSTAINER)).thenReturn(List.of(poll));

        mock.perform(get("/api/poll/usersaccessible"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].allowedUsersCount", is(0)));
    }

    @Test
    void getOwnedPolls( ) throws Exception {
        when(userService.getUserFromAuthentication()).thenReturn(user);
        when(collectionRepository.findCollectionById(collection.getId())).thenReturn(Optional.of(collection));
        when(repository.findOwnedPolls(user.getId(), CollUserType.FOUNDER, collection.getId())).thenReturn(List.of(poll));

        mock.perform(get("/api/poll/collectionpolls")
                .param("collectionid", objectMapper.writeValueAsString(1L)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].allowedUsersCount", is(0)));
    }

    @Test
    void getPollsHistory( ) throws Exception {
        when(userService.getUserFromAuthentication()).thenReturn(user);
        when(repository.findPollsHistory(user.getId(), CollUserType.SUSTAINER)).thenReturn(List.of(poll));

        mock.perform(get("/api/poll/pollshistory"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].allowedUsersCount", is(0)));
    }
}