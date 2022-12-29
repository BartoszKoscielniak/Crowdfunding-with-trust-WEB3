package com.crowdfunding.crowdfundingapi.user.registration;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/user/register")
@AllArgsConstructor
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    @PostMapping
    public ResponseEntity<Map<String, String>> registerUser(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String privateKey,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String phoneNumber)  {
        return userRegistrationService.registerUser(name, surname, privateKey, password, email, phoneNumber);
    }
}
