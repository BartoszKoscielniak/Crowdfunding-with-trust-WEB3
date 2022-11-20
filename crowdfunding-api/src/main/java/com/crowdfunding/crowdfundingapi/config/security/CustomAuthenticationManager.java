package com.crowdfunding.crowdfundingapi.config.security;

import com.crowdfunding.crowdfundingapi.user.User;
import com.crowdfunding.crowdfundingapi.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String publicAddress = authentication.getPrincipal().toString();
        String signature = authentication.getCredentials().toString();

        Optional<User> user = userService.getUserByPublicAddress(publicAddress);
        if (user.isEmpty()){
            throw new BadCredentialsException("User not found");
        }

        if (!Objects.equals(user.get().getPublicAddress(), publicAddress)){
            throw new BadCredentialsException("Incorrect public address");
        }



        return UsernamePasswordAuthenticationToken.authenticated(publicAddress, signature, user.get().getAuthorities());
    }
}
