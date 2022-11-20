package com.crowdfunding.crowdfundingapi.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/user")
@AllArgsConstructor
public class UserController {
    /*
    *  TODO: historia transakcji
    *  TODO: informacje o uzytkowniku
    *  TODO: przelewanie srodkow
    *  TODO: przechowywanie srodkow
    *  TODO: */
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping(path = "/nonce")
    public Optional<String> getUsersNonce(
            @RequestParam String publicAddress,
            @RequestParam String password){

        return userService.getUsersNonce(publicAddress, password);
    }
}
