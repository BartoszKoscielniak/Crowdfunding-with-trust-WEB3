package com.crowdfunding.crowdfundingapi.user.registration;

import com.crowdfunding.crowdfundingapi.user.UserRepository;
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

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserRegistrationControllerTest {

    @Autowired
    MockMvc mock;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @InjectMocks
    private UserRegistrationService userRegistrationService;

    @Test
    void registerUser( ) throws Exception {
        when(userRepository.findUserByPhoneNumberOrEmailOrPublicAddress("555666777","test@emial.com","0x9F1E52ac3a936066d4869b4c37DfBb590Ac7e1a0")).thenReturn(Optional.empty());

        mock.perform(post("/api/user/register")
                .param("name", "name")
                .param("surname", "surname")
                .param("privateKey", "dc188d2d24b0735c1301bbecc4c6f4212289c456084f329a77e2c7da8fd9076d")
                .param("password", "testPassword123%%^&")
                .param("email", "test@emial.com")
                .param("phoneNumber", "555666777"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result", is("Successfully registered!")))
                .andExpect(jsonPath("$.status", is("success")));
    }
}