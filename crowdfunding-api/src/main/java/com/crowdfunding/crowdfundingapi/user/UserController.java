package com.crowdfunding.crowdfundingapi.user;

import com.crowdfunding.crowdfundingapi.collection.CollectionType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/user")
@AllArgsConstructor
public class UserController {
    /*
    *  TODO: moze vote history w solidity
    *  TODO: zapobieganie oszustwu przy glosowaniu, uzytkownik moze z innego adresu wplacac srodki zeby miec wiekszosc w glosowaniu
    *  TODO: ustawic dostepy do endpointow w security
    *  TODO: moze KYC? trzeba ogranczyc dostep do apki zwiekszyc bezpieczenstwo
    */
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(
            @PathVariable Long id){
        return userService.getUser(id);
    }

    @GetMapping(path = "/nonce")
    public ResponseEntity<Map<String, String>> getUsersNonce(
            @RequestParam String publicAddress,
            @RequestParam String password){
        return userService.getUsersNonce(publicAddress, password);
    }

    @PutMapping(path = "/password")
    public ResponseEntity<Map<String, String>> updateCollection(
            @RequestParam String oldPassword,
            @RequestParam String newPassword){
        return userService.changePassword(oldPassword, newPassword);
    }
}
