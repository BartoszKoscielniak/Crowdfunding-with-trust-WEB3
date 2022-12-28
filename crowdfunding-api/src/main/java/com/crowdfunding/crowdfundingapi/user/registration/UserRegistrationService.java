package com.crowdfunding.crowdfundingapi.user.registration;

import com.crowdfunding.crowdfundingapi.config.PasswordConfig;
import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import com.crowdfunding.crowdfundingapi.config.security.Nonce;
import com.crowdfunding.crowdfundingapi.user.User;
import com.crowdfunding.crowdfundingapi.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.abi.Utils;
import org.web3j.utils.Numeric;

import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;
    private final Nonce nonce;
    public ResponseEntity registerUser(String publicAddress, String password) throws NoSuchAlgorithmException {
        Optional<User> optionalUser = userRepository.findUserByPublicAddress(publicAddress);

        if (publicAddress.length() != 42)//TODO:validtion
        {
            return ResponseEntity.badRequest().body(new PreparedResponse().getFailureResponse("Incorrect address format"));
        }

        if (optionalUser.isEmpty()){
            if (password.length() < 8){
                return ResponseEntity.badRequest().body(new PreparedResponse().getFailureResponse("Incorrect password format"));
            }

            byte[] privateKeyBytes = AdvancedEncryptionStandard.encrypt(privateKey.getBytes());

            User newUser = new User(publicAddress, name, lastname, email, phoneNumber, privateKeyBytes, passwordConfig.passwordEncoder().encode(password), nonce.generateNonce(), UserRole.USER);
            userRepository.save(newUser);

            return ResponseEntity.ok(new PreparedResponse().getSuccessResponse("User added"));
        }else {

            return ResponseEntity.badRequest().body(new PreparedResponse().getFailureResponse("User with provided address already exist"));
        }
    }
}
