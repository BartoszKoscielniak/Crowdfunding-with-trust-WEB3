package com.crowdfunding.crowdfundingapi.config.security;

import com.crowdfunding.crowdfundingapi.user.User;
import com.crowdfunding.crowdfundingapi.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;

@AllArgsConstructor
@Configuration
public class Nonce {

    private final UserService userService;
    private final String PREFIX = "\u0019Ethereum Signed Message:\n";


    public String generateNonce() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");

        return "Your verification nonce to sign: " + secureRandom.nextInt(10, (int) Math.pow(2, 127));
    }

    public void changeNonce(String publicAddress) throws NoSuchAlgorithmException {
        User user = userService.getUserByPublicAddress(publicAddress).get();
        user.setNonce(generateNonce());
        userService.updateUser(user);
    }

    public boolean verifySignature(String publicAddress, String password, String signature) throws NoSuchAlgorithmException {
        String nonce = getNonce(publicAddress, password).getBody().get("result");

        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte[] r = Arrays.copyOfRange(signatureBytes, 0, 32);
        byte[] s = Arrays.copyOfRange(signatureBytes, 32, 64);
        byte v = (byte) ((signatureBytes[64] < 27) ? (signatureBytes[64] + 27) : signatureBytes[64]);

        String message = PREFIX + nonce.length() + nonce;
        byte[] hash = Hash.sha3(message.getBytes(StandardCharsets.UTF_8));

        Sign.SignatureData signatureData = new Sign.SignatureData( v, r, s);
        ECDSASignature es = new ECDSASignature(new BigInteger(1, signatureData.getR()), new BigInteger(signatureData.getS()));

        String recovered = "0x" + Keys.getAddress(Sign.recoverFromSignature(v - 27, es, hash));
        if (recovered.equalsIgnoreCase(publicAddress)){
            changeNonce(publicAddress);
            return true;
        }
        return false;
    }

    public ResponseEntity<Map<String, String>> getNonce(String publicAddress, String password){
        return userService.getUsersNonce(publicAddress, password);
    }
}
