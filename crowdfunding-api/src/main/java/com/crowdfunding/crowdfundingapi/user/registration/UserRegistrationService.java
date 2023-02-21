package com.crowdfunding.crowdfundingapi.user.registration;

import com.crowdfunding.crowdfundingapi.config.PasswordConfig;
import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import com.crowdfunding.crowdfundingapi.config.security.AdvancedEncryptionStandard;
import com.crowdfunding.crowdfundingapi.user.User;
import com.crowdfunding.crowdfundingapi.user.UserRepository;
import com.crowdfunding.crowdfundingapi.user.UserRole;
import com.crowdfunding.crowdfundingapi.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordConfig passwordConfig;
    @Value("${crowdfunding.pub.address}")
    private String deploymentAddress;

    public ResponseEntity<Map<String, String>> registerUser(String name, String lastname, String privateKey, String password, String email, String phoneNumber) {
        try {
            if (privateKey.length() != 64){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Invalid private key!"));
            }

            if (name.length() < 3 || lastname.length() < 3){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Each details should have at least length of 3."));
            }

            Credentials credentials = Credentials.create(privateKey);
            String publicAddress = credentials.getAddress().toLowerCase();
            email = email.toLowerCase();
            Optional<User> optionalUser = userRepository.findUserByPhoneNumberOrEmailOrPublicAddress(phoneNumber, email, publicAddress);

            if (optionalUser.isPresent()){
                if (optionalUser.get().getPublicAddress().equals(publicAddress)){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("User with provided address already exist!"));
                }

                if (optionalUser.get().getPhoneNumber().equals(phoneNumber)){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("User with provided phone number already exist!"));
                }

                if (optionalUser.get().getEmail().equals(email)){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("User with provided e-mail already exist!"));
                }
            }

            ResponseEntity<Map<String, String>> passwordValidationResult = userService.passwordValidation(password);
            if (passwordValidationResult.getStatusCode() == HttpStatus.BAD_REQUEST){
                return passwordValidationResult;
            }

            if (!Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])").matcher(email).matches()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Invalid email!"));
            }

            if (!Pattern.compile("^[0-9\\+]{9,15}$").matcher(phoneNumber).matches()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse("Invalid phone number!"));
            }

            byte[] privateKeyBytes = AdvancedEncryptionStandard.encrypt(privateKey.getBytes());

            User newUser;
            if (deploymentAddress.equalsIgnoreCase(publicAddress)){
                newUser = new User(publicAddress, name, lastname, email, phoneNumber, privateKeyBytes, passwordConfig.passwordEncoder().encode(password), UserRole.ADMIN);
            } else {
                newUser = new User(publicAddress, name, lastname, email, phoneNumber, privateKeyBytes, passwordConfig.passwordEncoder().encode(password), UserRole.USER);
            }
            userRepository.save(newUser);

            return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse("Successfully registered!"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PreparedResponse().getFailureResponse(e.getMessage()));
        }
    }
}
