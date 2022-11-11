package com.crowdfunding.crowdfundingapi.user;

import com.crowdfunding.crowdfundingapi.config.PasswordConfig;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static com.crowdfunding.crowdfundingapi.user.UserRole.USER;

@Configuration
@AllArgsConstructor
public class UserConfig {

    private final PasswordConfig passwordConfig;

    @Bean
    CommandLineRunner commandLineRunner (UserRepository repository) {
        return args -> {
            User Bartosz = new User(
                    "0x9F1E52ac3a936066d4869b4c37DfBb590Ac7e1a0",
                    passwordConfig.passwordEncoder().encode("0x9F1E52ac3a936066d4869b4c37DfBb590Ac7e1a0"),
                    USER,
                    true,
                    true,
                    true,
                    true
            );

            repository.saveAll(
                    List.of(Bartosz)
            );
        };
    }
}
