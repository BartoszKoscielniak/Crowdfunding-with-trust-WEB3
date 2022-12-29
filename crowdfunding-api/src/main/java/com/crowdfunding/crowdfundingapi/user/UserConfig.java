package com.crowdfunding.crowdfundingapi.user;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.collection.CollectionRepository;
import com.crowdfunding.crowdfundingapi.collection.CollectionType;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhase;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhaseRepository;
import com.crowdfunding.crowdfundingapi.config.PasswordConfig;
import com.crowdfunding.crowdfundingapi.config.security.Nonce;
import com.crowdfunding.crowdfundingapi.poll.Poll;
import com.crowdfunding.crowdfundingapi.poll.PollRepository;
import com.crowdfunding.crowdfundingapi.poll.PollState;
import com.crowdfunding.crowdfundingapi.support.CollUserRelation;
import com.crowdfunding.crowdfundingapi.support.CollUserType;
import com.crowdfunding.crowdfundingapi.support.RelationRepository;
import com.crowdfunding.crowdfundingapi.web3.Web3;
import com.crowdfunding.crowdfundingapi.web3.Web3Repository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

import static com.crowdfunding.crowdfundingapi.user.UserRole.USER;

@Configuration
@AllArgsConstructor
public class UserConfig {

    private final PasswordConfig passwordConfig;
    private final Nonce nonce;
    private final CollectionRepository collectionRepository;
    private final CollectionPhaseRepository collectionPhaseRepository;
    private final RelationRepository relationRepository;
    private final PollRepository pollRepository;
    private final Web3Repository web3Repository;

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
                    nonce.generateNonce(),
                    USER
            );

            User Crowdfunding = new User(
                    "0xe64Ea9Db707c40a6E502A8AE6613ffc222b30C3F".toLowerCase(),
                    "Crowdfunding",
                    "CEO",
                    "crowdfundingceo@gmail.com".toLowerCase(),
                    "+48111222333",
                    new byte[] {73, 22, 126, -115, -58, -48, 20, 47, -17, 2, 16, 72, 22, -104, 22, 37, -49, -10, 89, -84, -89, 102, 63, 58, -29, 56, 11, -111, -62, -124, -93, 123, -5, 72, -76, -19, -67, 63, 3, -60, -25, -69, -60, 120, -6, 40, 72, -40, -85, -33, 22, 42, -53, 114, 35, 118, 31, -89, -94, -15, 43, 55, 32, -105, -93, -62, 7, 16, 52, 77, -37, -8, 49, -116, -41, 86, 51, 35, -56, -21},
                    passwordConfig.passwordEncoder().encode("dc188d2d24b0735c1301bbecc4c6f4212289c456084f329a77e2c7da8fd9076d"),
                    nonce.generateNonce(),
                    USER
            );

            repository.saveAll(
                    List.of(Bartosz, Crowdfunding)
            );

            Collection testCollection1 = new Collection(
                    15.5,
                    "STARTUP1",
                    CollectionType.STARTUP,
                    new CollUserRelation(Crowdfunding, CollUserType.FOUNDER)
            );

            Collection testCollection2 = new Collection(
                    9.5,
                    "CHARITY1",
                    CollectionType.CHARITY,
                    new CollUserRelation(Bartosz, CollUserType.FOUNDER)
            );

            collectionRepository.saveAll(
                    List.of(testCollection1 ,testCollection2)
            );

            CollUserRelation relation1 = new CollUserRelation(
                    Crowdfunding,
                    testCollection1,
                    CollUserType.SUSTAINER
            );

            relationRepository.saveAll(
                    List.of(relation1)
            );

            Poll poll1 = new Poll(PollState.NOT_ACTIVATED);
            Poll poll2 = new Poll(PollState.NOT_ACTIVATED);
            Poll poll3 = new Poll(PollState.NOT_ACTIVATED);
            Poll poll4 = new Poll(PollState.NOT_ACTIVATED);

            CollectionPhase phase1 = new CollectionPhase(
                    5.5,
                    "PHASE DESC 1",
                    testCollection1,
                    LocalDateTime.now().plusDays(15)
            );

            CollectionPhase phase2 = new CollectionPhase(
                    1.0,
                    "PHASE DESC 2",
                    testCollection1,
                    LocalDateTime.now().plusDays(22)
            );

            CollectionPhase phase3 = new CollectionPhase(
                    8.89,
                    "PHASE DESC 3",
                    testCollection1,
                    LocalDateTime.now().plusDays(32)
            );

            CollectionPhase phase4 = new CollectionPhase(
                    8.89,
                    "PHASE DESC 3",
                    testCollection2,
                    LocalDateTime.now().plusDays(40)
            );

            collectionPhaseRepository.saveAll(
                    List.of(phase1, phase2, phase3, phase4)
            );

            poll1.setCollectionPhase(phase1);
            poll2.setCollectionPhase(phase2);
            poll3.setCollectionPhase(phase3);
            poll4.setCollectionPhase(phase4);

            pollRepository.saveAll(
                    List.of(poll1, poll2, poll3, poll4)
            );

            Web3 web31 = new Web3("Funds"       , "0x886520dd832c59fe6923baa0d8e30a12e29099c6");
            Web3 web32 = new Web3("Commission"  , "0x2ddf9ed285d762736917747694ed036851dfeaf4");
            Web3 web33 = new Web3("Advertise"   , "0x6052d96c8778eec6ea9961b21b491dba044abe7d");

            web3Repository.saveAll(
                    List.of(  web31, web32, web33 )
            );
        };
    }
}
