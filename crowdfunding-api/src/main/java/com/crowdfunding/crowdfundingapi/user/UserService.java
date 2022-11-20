package com.crowdfunding.crowdfundingapi.user;

import com.crowdfunding.crowdfundingapi.config.PasswordConfig;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Optional<String> getUsersNonce(String publicAddress, String password) {
        Optional<User> user = userRepository.findUserByPublicAddress(publicAddress);
        if (user.isPresent()){
            if (passwordConfig.passwordEncoder().matches(password ,user.get().getPassword())){
                return Optional.ofNullable(user.get().getNonce());
            }
        }
        return Optional.empty();
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
}
