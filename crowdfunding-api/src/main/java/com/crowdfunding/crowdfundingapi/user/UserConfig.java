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
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
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

    @Bean
    CommandLineRunner commandLineRunner (UserRepository repository) {
        return args -> {
            User Bartosz = new User(
                    "0x9F1E52ac3a936066d4869b4c37DfBb590Ac7e1a0",
                    passwordConfig.passwordEncoder().encode("password"),
                    nonce.generateNonce(),
                    USER,
                    true,
                    true,
                    true,
                    true
            );

            User Supporter = new User(
                    "0x9F1E52ac3a936466d4869b4c37DfBb590Ac7e1a0",
                    passwordConfig.passwordEncoder().encode("password"),
                    nonce.generateNonce(),
                    USER,
                    true,
                    true,
                    true,
                    true
            );

            repository.saveAll(
                    List.of(Bartosz, Supporter)
            );

            Collection testCollection1 = new Collection(
                    15.5,
                    "STARTUP1",
                    CollectionType.STARTUP,
                    new CollUserRelation(Bartosz, CollUserType.FOUNDER)
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
                    Supporter,
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
                    testCollection1
            );

            CollectionPhase phase2 = new CollectionPhase(
                    1.0,
                    "PHASE DESC 2",
                    testCollection1
            );

            CollectionPhase phase3 = new CollectionPhase(
                    8.89,
                    "PHASE DESC 3",
                    testCollection1
            );

            CollectionPhase phase4 = new CollectionPhase(
                    8.89,
                    "PHASE DESC 3",
                    testCollection2
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


        };
    }
}
