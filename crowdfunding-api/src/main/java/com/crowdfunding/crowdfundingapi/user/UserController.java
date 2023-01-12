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
    /*
    *  TODO: zapobieganie oszustwu przy glosowaniu, uzytkownik moze z innego adresu wplacac srodki zeby miec wiekszosc w glosowaniu
    *  TODO: ustawic dostepy do endpointow w security
    *  TODO: zahasuj/zakoduj signauter albo go usun'
    *  TODO: umieszczanie w jsonie promowanej zbiorki najpeirw
    *  TODO: sprawdzanie czy juz nie zakonczona zbiorka
    *  TODO; kilka powiadomien na raz front
    *  TODO: w properties zmiana ustawien
    *  TODO: zrobic cos z 403 status
    *  TODO: spradzenie czy fraud i wyslanie funduszy sprawdzenie glosowania itd zakonczenie zbiorki po glsoowaniu
    *  TODO: jak zakoncza sie wszystkie fazy nie pozwalaj przegladac zbiorki
    *  TODO: proof of evidence do phase coll
    *  TODO: react router ograniczyc dostep bez lgoowania
    *  TODO: wysylanie pliku z evidence
    *  TODO: przerobic widok na ongoing polls
    *  TODO: w aboutus wrzucic info o kotnraktach i adresy
    *  TODO: zapisywanie na blockchainie fraud end itp
    *  TODO: uruchomienie srpawdzenia wyniku glosowania
    */
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
