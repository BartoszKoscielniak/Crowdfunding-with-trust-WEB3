package com.crowdfunding.crowdfundingapi.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping(path = "/me")
    public ResponseEntity<User> getMyInformation(){
        return userService.getMyInformation();
    }

    @GetMapping(path = "/auth")
    public ResponseEntity<User> getAuthUsers(){
        return userService.getAuthUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(
            @PathVariable Long id){
        return userService.getUser(id);
    }

    @PutMapping(path = "/password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword){
        return userService.changePassword(oldPassword, newPassword);
    }

    @PutMapping(path = "/details")
    public ResponseEntity<Map<String, String>> changeDetails(
            @RequestParam String name,
            @RequestParam String lastname){
        return userService.changeDetails(name, lastname);
    }
}
