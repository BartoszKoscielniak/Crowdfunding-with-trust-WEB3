package com.crowdfunding.crowdfundingapi.collection.phase;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.collection.CollectionRepository;
import com.crowdfunding.crowdfundingapi.poll.PollRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CollectionPhaseControllerTest {

    @Autowired
    private MockMvc mock;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private CollectionPhaseRepository repository;
    @MockBean
    private  CollectionRepository collectionRepository;
    @MockBean
    private PollRepository pollRepository;
    @InjectMocks
    private CollectionPhaseService service;

    private final CollectionPhase collectionPhaseOne = CollectionPhase.builder()
            .phaseName("Test Phase 1")
            .goal(10.0)
            .id(1L)
            .proofOfEvidence("https://www.google.com/")
            .description("Test Desc 1")
            .till(LocalDateTime.now().plusDays(1))
            .collection(new Collection(1L))
            .build();
    private final CollectionPhase collectionPhaseTwo = CollectionPhase.builder()
            .phaseName("Test Phase 2")
            .goal(15.0)
            .id(2L)
            .proofOfEvidence("https://www.google.com/")
            .description("Test Desc 2")
            .till(LocalDateTime.now().plusDays(5))
            .collection(new Collection(2L))
            .build();;

    @Test
    public void getPhase_OK( ) throws Exception {
        when(repository.findPhaseById(1L)).thenReturn(Optional.of(collectionPhaseOne));

        mock.perform(get("/api/phase/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].goal", is(10.0)))
                .andExpect(jsonPath("$.[0].phaseName", is("Test Phase 1")));
    }

    @Test
    public void getPhase_NOT_FOUND( ) throws Exception {
        mock.perform(get("/api/phase/{id}", 0))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCollectionPhases_OK( ) throws Exception {
        when(repository.findPhaseByCollectionId(1L)).thenReturn(List.of(collectionPhaseOne, collectionPhaseTwo));

        mock.perform(get("/api/phase/collectionphases/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].goal", is(10.0)))
                .andExpect(jsonPath("$.[0].phaseName", is("Test Phase 1")))
                .andExpect(jsonPath("$.[1].id", is(2)))
                .andExpect(jsonPath("$.[1].goal", is(15.0)))
                .andExpect(jsonPath("$.[1].phaseName", is("Test Phase 2")));
    }

    @Test
    public void getCollectionPhases_NOT_FOUND( ) throws Exception {
        mock.perform(get("/api/phase/collectionphases/{id}", 0))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addPhase_OK( ) throws Exception {
        when(collectionRepository.findCollectionById(1L)).thenReturn(Optional.of(new Collection(1L)));

        mock.perform(post("/api/phase")
                .contentType(MediaType.APPLICATION_JSON)
                .param("goal", objectMapper.writeValueAsString(collectionPhaseOne.getGoal()))
                .param("description", collectionPhaseOne.getDescription())
                .param("name", collectionPhaseOne.getPhaseName())
                .param("deadline", "2025-05-05")
                .param("collectionId", objectMapper.writeValueAsString(1L))
                .param("proofofevidence", collectionPhaseOne.getProofOfEvidence()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is("Phase added")))
                .andExpect(jsonPath("$.status", is("success")));
    }

    @Test
    public void addPhase_NOT_FOUND( ) throws Exception {
        mock.perform(post("/api/phase")
                .contentType(MediaType.APPLICATION_JSON)
                .param("goal", objectMapper.writeValueAsString(collectionPhaseOne.getGoal()))
                .param("description", collectionPhaseOne.getDescription())
                .param("name", collectionPhaseOne.getPhaseName())
                .param("deadline", "2025-05-05")
                .param("collectionId", objectMapper.writeValueAsString(1L))
                .param("proofofevidence", collectionPhaseOne.getProofOfEvidence()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Collection with provided ID not found")))
                .andExpect(jsonPath("$.status", is("fail")));
    }

    @Test
    public void updatePhase_OK( ) throws Exception {
        when(repository.findPhaseById(1L)).thenReturn(Optional.of(collectionPhaseOne));

        mock.perform(put("/api/phase/{id}", 1)
                .param("basedescription", "Updated description"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result", is("Phase updated")))
                .andExpect(jsonPath("$.status", is("success")));
    }

    @Test
    public void updatePhase_NOT_FOUND( ) throws Exception {
        mock.perform(put("/api/phase/{id}", 1)
                .param("basedescription", "Updated description"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("Phase with provided ID not found")))
                .andExpect(jsonPath("$.status", is("fail")));
    }

    @Test
    public void deletePhase_OK( ) throws Exception {
        when(repository.findPhaseById(1L)).thenReturn(Optional.of(collectionPhaseOne));

        mock.perform(delete("/api/phase/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result", is("Phase deleted")))
                .andExpect(jsonPath("$.status", is("success")));
    }

    @Test
    public void deletePhase_NOT_FOUND( ) throws Exception {
        mock.perform(delete("/api/phase/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("Phase with provided ID not found")))
                .andExpect(jsonPath("$.status", is("fail")));
    }
}