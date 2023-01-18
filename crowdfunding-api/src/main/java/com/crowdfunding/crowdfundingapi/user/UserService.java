package com.crowdfunding.crowdfundingapi.user;

import com.crowdfunding.crowdfundingapi.config.PasswordConfig;
import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import lombok.AllArgsConstructor;
import org.passay.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private final PasswordConfig passwordConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByPublicAddress(username)
                .orElseThrow(() ->
                    new UsernameNotFoundException(String.format("User: %s not found", username))
                );
    }

    public ResponseEntity<List<User>> getAllUsers( ) {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }

    public ResponseEntity<User> getAuthUsers( ) {
        User user = userRepository.findUserByPublicAddress(getUserFromAuthentication().getPublicAddress()).get();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    public Optional<User> getUserByPublicAddress(String publicAddress){
        return userRepository.findUserByPublicAddress(publicAddress);
    }

    public void updateUser(User user){
        userRepository.save(user);
    }

    public User getUserFromAuthentication( ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findUserByPublicAddress(authentication.getName()).get();
    }

    public ResponseEntity<User> getUser(Long userId) {
        Optional<User> user = userRepository.findUserById(userId);
        if (user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }

    public ResponseEntity<Map<String, String>> changePassword(String oldPassword, String newPassword) {
        User user = getUserFromAuthentication();
        if (!passwordConfig.passwordEncoder().matches(oldPassword ,user.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Incorrect old password."));
        }

        ResponseEntity<Map<String, String>> passwordValidationResult = passwordValidation(newPassword);
        if (passwordValidationResult.getStatusCode() == HttpStatus.BAD_REQUEST){
            return passwordValidationResult;
        }
        user.setPassword(passwordConfig.passwordEncoder().encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Password changed."));
    }

    public ResponseEntity<Map<String, String>> passwordValidation(String password){
        PasswordValidator passwordValidator = new PasswordValidator(List.of(
                new LengthRule(10, 25),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 2),
                new CharacterRule(EnglishCharacterData.Special, 2)
        ));
        RuleResult result = passwordValidator.validate(new PasswordData(password));

        if (!result.isValid()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse(passwordValidator.getMessages(result).get(0)));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<User> getMyInformation( ) {
        User authUser = getUserFromAuthentication();
        Optional<User> user = userRepository.findUserByPublicAddress(authUser.getPublicAddress());
        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }

    public ResponseEntity<Map<String, String>> changeDetails(String name, String lastname) {
        User userAuth = getUserFromAuthentication();
        if (name.length() < 3 || lastname.length() < 3){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Each details should have at least length of 3."));
        }

        if (name.equals(userAuth.getName()) || lastname.equals(userAuth.getLastname())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Details are the same!"));
        }

        User user = userRepository.findUserById(userAuth.getId()).get();
        user.setName(name);
        user.setLastname(lastname);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Details changed"));
    }
}
