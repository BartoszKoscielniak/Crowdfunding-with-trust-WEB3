package com.crowdfunding.crowdfundingapi.user.registration;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(path = "/api/user/register")
@AllArgsConstructor
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    @PostMapping
    public ResponseEntity registerUser(@RequestParam String publicAddress, @RequestParam String password) throws NoSuchAlgorithmException {
        return userRegistrationService.registerUser(publicAddress, password);
    }
}
