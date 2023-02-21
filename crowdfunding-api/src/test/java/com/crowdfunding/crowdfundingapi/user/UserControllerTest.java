package com.crowdfunding.crowdfundingapi.user;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.config.PasswordConfig;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserControllerTest {

    @Autowired
    MockMvc mock;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private UserRepository repository;
    @MockBean
    Authentication authentication;
    @MockBean
    SecurityContext securityContext;
    @InjectMocks
    private UserService service;
    private final PasswordConfig passwordConfig = new PasswordConfig();
    private final User userOne = User.builder()
            .id(1L)
            .publicAddress("123")
            .password(passwordConfig.passwordEncoder().encode("123"))
            .build();

    @Test
    void getAllUsers( ) throws Exception {
        when(repository.findAll()).thenReturn(List.of(userOne));

        mock.perform(get("/api/user"))
                //.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(1)));
    }

    @Test
    void getMyInformation( ) throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(repository.findUserByPublicAddress(userOne.getName())).thenReturn(Optional.of(userOne));

        mock.perform(get("/api/user/me"))
                //.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void changePassword( ) throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(repository.findUserByPublicAddress(userOne.getName())).thenReturn(Optional.of(userOne));

        mock.perform(put("/api/user/password")
                .param("oldPassword", "123")
                .param("newPassword", "newPassword12334%^&"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result", is("Password changed.")))
                .andExpect(jsonPath("$.status", is("success")));
    }

    @Test
    void changeDetails( ) throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(repository.findUserByPublicAddress(userOne.getName())).thenReturn(Optional.of(userOne));
        when(repository.findUserById(userOne.getId())).thenReturn(Optional.of(userOne));

        mock.perform(put("/api/user/details")
                        .param("name", "name")
                        .param("lastname", "lastname"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result", is("Details changed.")))
                .andExpect(jsonPath("$.status", is("success")));
    }
}