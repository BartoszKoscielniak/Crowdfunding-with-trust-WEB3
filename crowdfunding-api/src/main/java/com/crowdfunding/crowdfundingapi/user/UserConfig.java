package com.crowdfunding.crowdfundingapi.user;

import com.crowdfunding.crowdfundingapi.config.PasswordConfig;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.crowdfunding.crowdfundingapi.user.UserRole.ADMIN;
import static com.crowdfunding.crowdfundingapi.user.UserRole.USER;

@Configuration
@AllArgsConstructor
public class UserConfig {

    private final PasswordConfig passwordConfig;

    @Bean
    CommandLineRunner commandLineRunner (UserRepository repository) {
        return args -> {

        };
    }
}