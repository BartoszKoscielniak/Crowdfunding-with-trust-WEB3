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

    public List<User> getAllUsers( ) {
        return userRepository.findAll();
    }

    public ResponseEntity<Map<String, String>> getUsersNonce(String publicAddress, String password) {
        Optional<User> user = userRepository.findUserByPublicAddress(publicAddress);
        if (user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("User with provided address not found"));
        }

        if (!passwordConfig.passwordEncoder().matches(password ,user.get().getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Incorrect password"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(user.get().getNonce()));
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreparedResponse().getFailureResponse("Incorrect password"));
        }

        ResponseEntity<Map<String, String>> passwordValidationResult = passwordValidation(newPassword);
        if (passwordValidationResult.getStatusCode() == HttpStatus.BAD_REQUEST){
            return passwordValidationResult;
        }
        user.setPassword(passwordConfig.passwordEncoder().encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).build();
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
}
