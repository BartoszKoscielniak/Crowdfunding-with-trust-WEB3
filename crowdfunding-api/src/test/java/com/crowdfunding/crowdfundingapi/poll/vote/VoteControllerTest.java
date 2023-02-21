package com.crowdfunding.crowdfundingapi.poll.vote;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhase;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhaseRepository;
import com.crowdfunding.crowdfundingapi.poll.Poll;
import com.crowdfunding.crowdfundingapi.poll.PollState;
import com.crowdfunding.crowdfundingapi.support.CollUserRelation;
import com.crowdfunding.crowdfundingapi.support.CollUserType;
import com.crowdfunding.crowdfundingapi.support.RelationRepository;
import com.crowdfunding.crowdfundingapi.user.User;
import com.crowdfunding.crowdfundingapi.user.UserService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VoteControllerTest {

    @Autowired
    MockMvc mock;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private VoteRepository repository;
    @MockBean
    private CollectionPhaseRepository collectionPhaseRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private RelationRepository relationRepository;
    @InjectMocks
    private VoteService service;

    private final Poll poll = Poll.builder()
            .id(1L)
            .startDate(LocalDateTime.now().minusDays(1))
            .endDate(LocalDateTime.now().plusDays(1))
            .state(PollState.IN_PROCESS)
            .votes(List.of())
            .build();
    private final CollectionPhase collectionPhaseOne = CollectionPhase.builder()
            .phaseName("Test Phase 1")
            .goal(10.0)
            .id(1L)
            .proofOfEvidence("https://www.google.com/")
            .description("Test Desc 1")
            .till(LocalDateTime.now().plusDays(1))
            .collection(new Collection(1L))
            .poll(poll)
            .build();

    private final User user = User.builder()
            .id(1L)
            .publicAddress("123")
            .build();

    @Test
    void addVote( ) throws Exception {
        when(userService.getUserFromAuthentication()).thenReturn(user);
        when(collectionPhaseRepository.findPhaseById(collectionPhaseOne.getId())).thenReturn(Optional.of(collectionPhaseOne));
        when(relationRepository.findRelationByUseridPhaseid(user.getId(), collectionPhaseOne.getId())).thenReturn(Optional.of(new CollUserRelation(user, new Collection(1L), CollUserType.SUSTAINER)));
        when(repository.checkUserVoteExist(user.getId(), collectionPhaseOne.getPoll().getId())).thenReturn(Optional.empty());

        mock.perform(post("/api/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .param("phaseId", objectMapper.writeValueAsString(collectionPhaseOne.getId()))
                .param("result", "ACCEPTED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is("Vote added.")))
                .andExpect(jsonPath("$.status", is("success")));
    }
}