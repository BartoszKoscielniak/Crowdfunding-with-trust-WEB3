package com.crowdfunding.crowdfundingapi.collection;

import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhase;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhaseRepository;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhaseService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CollectionControllerTest {

    @Autowired
    MockMvc mock;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private CollectionRepository repository;
    @MockBean
    private CollectionPhaseRepository phaseRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private RelationRepository relationRepository;
    @MockBean
    private CollectionPhaseService phaseService;
    @InjectMocks
    private CollectionService service;
    private final CollectionPhase phase = CollectionPhase.builder()
            .id(1L)
            .phaseName("Name should be at least 10 characters")
            .description("The required equipment is world-class medical equipment, used by the best hospitals around the world. ")
            .build();
    private final Collection collectionOne = Collection.builder()
            .id(1L)
            .collectionName("Collection Test 1")
            .actualFunds(0.0)
            .collectionType(CollectionType.STARTUP)
            .baseDescription("The required equipment is world-class medical equipment, used by the best hospitals around the world.")
            .promoted(false)
            .promoTo(null)
            .state(State.PUBLISHED)
            .goal(10.0)
            .collUserRelations(List.of())
            .collectionPhase(List.of(phase))
            .build();

    private final Collection collectionTwo = Collection.builder()
            .id(1L)
            .collectionName("Collection Test 2")
            .actualFunds(0.0)
            .collectionType(CollectionType.STARTUP)
            .baseDescription("Base Desc Test")
            .promoted(false)
            .promoTo(null)
            .state(State.PUBLISHED)
            .goal(10.0)
            .build();

    private final Collection collectionThree = Collection.builder()
            .id(1L)
            .collectionName("Collection Test 3")
            .actualFunds(0.0)
            .collectionType(CollectionType.CHARITY)
            .baseDescription("Base Desc Test")
            .promoted(false)
            .promoTo(null)
            .state(State.PUBLISHED)
            .goal(10.0)
            .build();

    private final User user = User.builder()
            .id(1L)
            .publicAddress("123")
            .build();

    @Test
    void getCollections( ) throws Exception {
        when(repository.findCollectionById(1L)).thenReturn(Optional.of(collectionOne));

        mock.perform(get("/api/collection/{id}", 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.collectionName", is(collectionOne.getCollectionName())))
                .andExpect(jsonPath("$.baseDescription", is(collectionOne.getBaseDescription())));
    }

    @Test
    void getOwnedCollections( ) throws Exception {
        when(userService.getUserFromAuthentication()).thenReturn(user);
        when(relationRepository.findCollectionsByRelation(CollUserType.FOUNDER, user.getPublicAddress())).thenReturn(List.of(collectionOne));

        mock.perform(get("/api/collection/owned"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].collectionName", is(collectionOne.getCollectionName())))
                .andExpect(jsonPath("$.[0].baseDescription", is(collectionOne.getBaseDescription())));
    }

    @Test
    void getAllCollections( ) throws Exception {
        when(repository.findAllByPromo()).thenReturn(List.of(collectionOne, collectionTwo, collectionThree));

        mock.perform(get("/api/collection"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mock.perform(get("/api/collection")
                .param("name", "Collection Test 2"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].collectionName", is("Collection Test 2")));

        mock.perform(get("/api/collection")
                .param("type", "CHARITY"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].collectionName", is("Collection Test 3")));
    }

    @Test
    void addCollection( ) throws Exception {
        when(userService.getUserFromAuthentication()).thenReturn(user);
        when(repository.findCollectionByName(collectionOne.getCollectionName())).thenReturn(Optional.empty());

        mock.perform(post("/api/collection")
                .contentType(MediaType.APPLICATION_JSON)
                .param("description", collectionOne.getBaseDescription())
                .param("name", collectionOne.getCollectionName())
                .param("type", "CHARITY")
                .param("phase1name", phase.getPhaseName())
                .param("phase1description", phase.getDescription())
                .param("phase1till", "2078-01-01")
                .param("phase1goal", objectMapper.writeValueAsString(10.0)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is("Collection added")))
                .andExpect(jsonPath("$.status", is("success")));
    }

    @Test
    void updateCollection( ) throws Exception {
        when(repository.findCollectionById(1L)).thenReturn(Optional.of(collectionOne));

        mock.perform(put("/api/collection/{id}", 1)
                .param("basedescription", "Updated description"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result", is("Collection updated")))
                .andExpect(jsonPath("$.status", is("success")));
    }

    @Test
    void deleteCollection( ) throws Exception {
        when(repository.findCollectionById(1L)).thenReturn(Optional.of(collectionOne));
        when(userService.getUserFromAuthentication()).thenReturn(user);
        when(phaseService.getCollectionFounder(1L)).thenReturn(ResponseEntity.status(HttpStatus.OK).body(user));

        mock.perform(delete("/api/collection/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result", is("Collection deleted")))
                .andExpect(jsonPath("$.status", is("success")));
    }

    @Test
    void publishCollection( ) throws Exception {
        when(repository.findCollectionById(1L)).thenReturn(Optional.of(collectionOne));
        when(userService.getUserFromAuthentication()).thenReturn(user);
        when(phaseService.getCollectionFounder(1L)).thenReturn(ResponseEntity.status(HttpStatus.OK).body(user));

        mock.perform(post("/api/collection/publish")
                .param("collectionId", objectMapper.writeValueAsString(1L)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result", is("Collection published")))
                .andExpect(jsonPath("$.status", is("success")));
    }
}