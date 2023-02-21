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
            User Bartosz = new User(
                    "0x9F1E52ac3a936066d4869b4c37DfBb590Ac7e1a0".toLowerCase(),
                    "Bartosz",
                    "Koscielniak",
                    "bartoszk@gmail.com".toLowerCase(),
                    "+48555444333",
                    new byte[] {26, 68, 121, -47, -97, -121, 19, 41, -20, 15, 17, 64, 16, -104, 19, 114, -100, -13, 12, -4, -93, 49, 62, 108, -30, 53, 92, -60, -110, -119, -14, 46, -83, 31, -69, -30, -20, 104, 0, -112, -78, -70, -57, 38, -15, 43, 70, -113, -83, -37, 21, 123, -112, 32, 119, 34, 21, -16, -16, -85, 45, 100, 38, -64, 43, -36, 106, -37, -54, 3, 120, 32, 31, -27, 44, -75, -25, -18, -1, -40},
                    passwordConfig.passwordEncoder().encode("testHaslo312!@"),
                    USER
            );

            User Crowdfunding = new User(
                    "0xe64Ea9Db707c40a6E502A8AE6613ffc222b30C3F".toLowerCase(),
                    "Crowdfunding",
                    "CEO",
                    "crowdfundingceo@gmail.com".toLowerCase(),
                    "+48111222333",
                    new byte[] {73, 22, 126, -115, -58, -48, 20, 47, -17, 2, 16, 72, 22, -104, 22, 37, -49, -10, 89, -84, -89, 102, 63, 58, -29, 56, 11, -111, -62, -124, -93, 123, -5, 72, -76, -19, -67, 63, 3, -60, -25, -69, -60, 120, -6, 40, 72, -40, -85, -33, 22, 42, -53, 114, 35, 118, 31, -89, -94, -15, 43, 55, 32, -105, -93, -62, 7, 16, 52, 77, -37, -8, 49, -116, -41, 86, 51, 35, -56, -21},
                    passwordConfig.passwordEncoder().encode("testAdminHaslo312!@"),
                    ADMIN
            );

            if (repository.findUserByPublicAddress(Bartosz.getPublicAddress()).isEmpty()){
                repository.save(Bartosz);
            }

            if (repository.findUserByPublicAddress(Crowdfunding.getPublicAddress()).isEmpty()){
                repository.save(Crowdfunding);
            }
        };
    }
}