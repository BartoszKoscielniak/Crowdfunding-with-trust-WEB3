package com.crowdfunding.crowdfundingapi.user;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.collection.CollectionRepository;
import com.crowdfunding.crowdfundingapi.collection.CollectionType;
import com.crowdfunding.crowdfundingapi.collection.State;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhase;
import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhaseRepository;
import com.crowdfunding.crowdfundingapi.config.PasswordConfig;
import com.crowdfunding.crowdfundingapi.config.security.Nonce;
import com.crowdfunding.crowdfundingapi.poll.Poll;
import com.crowdfunding.crowdfundingapi.poll.PollRepository;
import com.crowdfunding.crowdfundingapi.poll.PollState;
import com.crowdfunding.crowdfundingapi.poll.vote.Vote;
import com.crowdfunding.crowdfundingapi.poll.vote.VoteRepository;
import com.crowdfunding.crowdfundingapi.poll.vote.VoteResult;
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

import static com.crowdfunding.crowdfundingapi.user.UserRole.ADMIN;
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
    private final VoteRepository voteRepository;

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
                    ADMIN
            );

            repository.saveAll(
                    List.of(Bartosz, Crowdfunding)
            );

            Collection testCollection1 = new Collection(
                    "AI Based Startup",
                    18.5,
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
                    CollectionType.STARTUP,
                    new CollUserRelation(Crowdfunding, CollUserType.FOUNDER)
            );

            Collection testCollection2 = new Collection(
                    "SPACEX",
                    9.5,
                    "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                    CollectionType.CHARITY,
                    new CollUserRelation(Bartosz, CollUserType.FOUNDER)
            );

            Collection testCollection3 = new Collection(
                    "TESLA",
                    15.5,
                    "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia.",
                    CollectionType.STARTUP,
                    new CollUserRelation(Crowdfunding, CollUserType.FOUNDER)
            );

            Collection testCollection4 = new Collection(
                    "NASA new cosmo station",
                    9.5,
                    "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form.",
                    CollectionType.CHARITY,
                    new CollUserRelation(Crowdfunding, CollUserType.FOUNDER)
            );

            Collection testCollection5 = new Collection(
                    "AI Startup",
                    15.5,
                    "here are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable.",
                    CollectionType.STARTUP,
                    new CollUserRelation(Crowdfunding, CollUserType.FOUNDER)
            );

            Collection testCollection6 = new Collection(
                    "SPACEX Poland Branch",
                    9.5,
                    "If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text.",
                    CollectionType.CHARITY,
                    new CollUserRelation(Bartosz, CollUserType.FOUNDER)
            );
            testCollection6.setPromoTo(LocalDateTime.now().plusDays(5));
            Collection testCollection7 = new Collection(
                    "UR Institute of Cosmology",
                    15.5,
                    "All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words.",
                    CollectionType.STARTUP,
                    new CollUserRelation(Crowdfunding, CollUserType.FOUNDER)
            );

            Collection testCollection8 = new Collection(
                    "Water for africa",
                    9.5,
                    "Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.",
                    CollectionType.CHARITY,
                    new CollUserRelation(Bartosz, CollUserType.FOUNDER)
            );

            Collection testCollection9 = new Collection(
                    "House After Fire Rebuilde",
                    9.5,
                    "CHARITY1",
                    CollectionType.CHARITY,
                    new CollUserRelation(Bartosz, CollUserType.FOUNDER)
            );

            Collection testCollection10 = new Collection(
                    "Save Rainforestse",
                    12.5,
                    "CHARITY1",
                    CollectionType.CHARITY,
                    new CollUserRelation(Bartosz, CollUserType.FOUNDER)
            );

            Collection testCollection11 = new Collection(
                    "Houston Homeless People",
                    5.5,
                    "CHARITY1",
                    CollectionType.CHARITY,
                    new CollUserRelation(Bartosz, CollUserType.FOUNDER)
            );

            Collection testCollection12 = new Collection(
                    "Africa Solar Panels",
                    55.5,
                    "CHARITY1",
                    CollectionType.CHARITY,
                    new CollUserRelation(Bartosz, CollUserType.FOUNDER)
            );
            testCollection5.setPromoted(true);
            testCollection5.setPromoTo(LocalDateTime.now().plusDays(15));
            testCollection1.setState(State.PUBLISHED);
            testCollection2.setState(State.PUBLISHED);
            testCollection3.setState(State.PUBLISHED);
            testCollection4.setState(State.PUBLISHED);
            testCollection5.setState(State.PUBLISHED);
            testCollection6.setState(State.PUBLISHED);
            testCollection7.setState(State.PUBLISHED);
            testCollection8.setState(State.PUBLISHED);
            testCollection9.setState(State.PUBLISHED);
            testCollection10.setState(State.PUBLISHED);
            testCollection11.setState(State.PUBLISHED);
            testCollection12.setState(State.PUBLISHED);
            collectionRepository.saveAll(
                    List.of(testCollection1, testCollection2, testCollection3, testCollection4,
                            testCollection5, testCollection6, testCollection7, testCollection8,
                            testCollection9, testCollection10, testCollection11, testCollection12)
            );

            CollUserRelation relation1 = new CollUserRelation(
                    Crowdfunding,
                    testCollection1,
                    CollUserType.SUSTAINER
            );

            relationRepository.saveAll(
                    List.of(relation1)
            );

            CollectionPhase phase1 = new CollectionPhase(
                    5.5,
                    "Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.",
                    "Headquarters",
                    testCollection1,
                    LocalDateTime.now().plusDays(15),
                    "https://etherscan.io/"
            );

            CollectionPhase phase2 = new CollectionPhase(
                    1.0,
                    "All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words.",
                    "Necessary hardware",
                    testCollection1,
                    LocalDateTime.now().plusDays(22),
                    "https://etherscan.io/"
            );

            CollectionPhase phase3 = new CollectionPhase(
                    8.89,
                    "If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text.",
                    "High-end research equipment",
                    testCollection1,
                    LocalDateTime.now().plusDays(32),
                    "https://etherscan.io/"
            );

            CollectionPhase phase34 = new CollectionPhase(
                    3.11,
                    "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form.",
                    "Upfront research costs",
                    testCollection1,
                    LocalDateTime.now().plusDays(42),
                    "https://etherscan.io/"
            );

            CollectionPhase phase4 = new CollectionPhase(
                    8.89,
                    "PHASE DESC 3",
                    "PHase title",
                    testCollection2,
                    LocalDateTime.now().plusDays(40),
                    "https://etherscan.io/"
            );

            CollectionPhase phase5 = new CollectionPhase(
                    testCollection4.getGoal(),
                    testCollection4.getCollectionName(),
                    "PHase title",
                    testCollection4,
                    LocalDateTime.now().plusDays(40),
                    "https://etherscan.io/"
            );

            CollectionPhase phase6 = new CollectionPhase(
                    testCollection4.getGoal(),
                    testCollection4.getCollectionName(),
                    "PHase title",
                    testCollection5,
                    LocalDateTime.now().plusDays(40),
                    "https://etherscan.io/"
            );

            CollectionPhase phase7 = new CollectionPhase(
                    testCollection4.getGoal(),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis suscipit viverra varius. Nam porta lacus vel posuere elementum. Pellentesque ligula velit, gravida vitae purus quis, fringilla aliquam sem. Suspendisse potenti. Suspendisse potenti. Suspendisse",
                    "Test phase 1",
                    testCollection6,
                    LocalDateTime.now().plusDays(40),
                    "https://etherscan.io/"
            );

            CollectionPhase phase75 = new CollectionPhase(
                    testCollection4.getGoal(),
                    "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut eget scelerisque massa. Fusce ultricies arcu at nulla auctor, nec tempus orci auctor.",
                    "Test phase 2",
                    testCollection6,
                    LocalDateTime.now().plusDays(40),
                    "https://etherscan.io/"
            );

            CollectionPhase phase755 = new CollectionPhase(
                    testCollection4.getGoal(),
                    "Curabitur dictum nibh ultricies purus pharetra congue. Maecenas eros orci, tincidunt non velit et, varius dapibus urna. Vestibulum diam nunc, luctus sit amet quam ut, volutpat dignissim lacus.",
                    "Test phase 3",
                    testCollection6,
                    LocalDateTime.now().plusDays(40),
                    "https://etherscan.io/"
            );

            CollectionPhase phase8 = new CollectionPhase(
                    testCollection4.getGoal(),
                    testCollection4.getCollectionName(),
                    "PHase title",
                    testCollection7,
                    LocalDateTime.now().plusDays(40),
                    "https://etherscan.io/"
            );

            CollectionPhase phase9 = new CollectionPhase(
                    testCollection4.getGoal(),
                    testCollection4.getCollectionName(),
                    "PHase title",
                    testCollection8,
                    LocalDateTime.now().plusDays(40),
                    "https://etherscan.io/"
            );

            CollectionPhase phase10 = new CollectionPhase(
                    testCollection4.getGoal(),
                    testCollection4.getCollectionName(),
                    "PHase title",
                    testCollection9,
                    LocalDateTime.now().plusDays(40),
                    "https://etherscan.io/"
            );

            CollectionPhase phase11 = new CollectionPhase(
                    testCollection4.getGoal(),
                    testCollection4.getCollectionName(),
                    "PHase title",
                    testCollection10,
                    LocalDateTime.now().plusDays(40),
                    "https://etherscan.io/"
            );

            CollectionPhase phase12 = new CollectionPhase(
                    testCollection4.getGoal(),
                    testCollection4.getCollectionName(),
                    "PHase title",
                    testCollection11,
                    LocalDateTime.now().plusDays(40),
                    "https://etherscan.io/"
            );

            CollectionPhase phase13 = new CollectionPhase(
                    testCollection4.getGoal(),
                    testCollection4.getCollectionName(),
                    "PHase title",
                    testCollection12,
                    LocalDateTime.now().plusDays(40),
                    "https://etherscan.io/"
            );

            CollectionPhase phase14 = new CollectionPhase(
                    testCollection3.getGoal(),
                    testCollection3.getCollectionName(),
                    "PHase title",
                    testCollection3,
                    LocalDateTime.now().plusDays(40),
                    "https://etherscan.io/"
            );

            collectionPhaseRepository.saveAll(
                    List.of(phase1, phase2, phase3, phase4, phase5, phase6, phase7, phase8,
                            phase9, phase10, phase11, phase12, phase13, phase14, phase34, phase75, phase755)
            );

            relationRepository.save(new CollUserRelation(Bartosz, testCollection1, phase1, CollUserType.SUSTAINER));
            relationRepository.save(new CollUserRelation(Bartosz, testCollection1, phase2, CollUserType.SUSTAINER));
            relationRepository.save(new CollUserRelation(Bartosz, testCollection3, phase14, CollUserType.SUSTAINER));

            Poll poll1 = new Poll(PollState.NOT_ACTIVATED);
            Poll poll2 = new Poll(PollState.NOT_ACTIVATED);
            Poll poll3 = new Poll(PollState.NOT_ACTIVATED);
            Poll poll4 = new Poll(PollState.NOT_ACTIVATED);
            Poll poll5 = new Poll(PollState.NOT_ACTIVATED);
            Poll poll6 = new Poll(PollState.NOT_ACTIVATED);
            Poll poll7 = new Poll(PollState.NOT_ACTIVATED);
            Poll poll8 = new Poll(PollState.NOT_ACTIVATED);
            Poll poll9 = new Poll(PollState.NOT_ACTIVATED);

            poll1.setCollectionPhase(phase1);
            poll2.setCollectionPhase(phase2);
            poll3.setCollectionPhase(phase3);
            poll4.setCollectionPhase(phase4);
            poll5.setCollectionPhase(phase7);
            poll5.setStartDate(LocalDateTime.now());
            poll5.setEndDate(LocalDateTime.now().plusDays(1));
            poll4.setEndDate(LocalDateTime.now().plusDays(1));
            poll3.setEndDate(LocalDateTime.now().plusDays(1));
            poll2.setEndDate(LocalDateTime.now().plusDays(1));
            poll1.setEndDate(LocalDateTime.now().plusDays(1));
            poll4.setStartDate(LocalDateTime.now());
            poll3.setStartDate(LocalDateTime.now());
            poll2.setStartDate(LocalDateTime.now());
            poll1.setStartDate(LocalDateTime.now());
            poll1.setState(PollState.IN_PROCESS);
            poll2.setState(PollState.NEGATIVE);

            poll6.setCollectionPhase(phase13);
            poll6.setState(PollState.NEGATIVE);
            poll7.setCollectionPhase(phase12);
            poll7.setState(PollState.POSITIVE);
            poll8.setCollectionPhase(phase11);
            poll8.setState(PollState.POSITIVE);
            poll9.setCollectionPhase(phase14);
            poll9.setState(PollState.POSITIVE);
            poll6.setStartDate(LocalDateTime.now());
            poll7.setStartDate(LocalDateTime.now());
            poll8.setStartDate(LocalDateTime.now());
            poll9.setStartDate(LocalDateTime.now());
            poll6.setEndDate(LocalDateTime.now().plusDays(1));
            poll7.setEndDate(LocalDateTime.now().plusDays(1));
            poll8.setEndDate(LocalDateTime.now().plusDays(1));
            poll9.setEndDate(LocalDateTime.now().plusDays(1));
            pollRepository.saveAll(
                    List.of(poll1, poll2, poll3, poll4, poll5, poll6, poll7, poll8, poll9)
            );

            voteRepository.save(new Vote(VoteResult.ACCEPTED, poll1, Bartosz));

            Web3 web31 = new Web3("Funds"       , "0x4c8fd932918ab2d546dfa6c8094f3712150e72a6");
            Web3 web32 = new Web3("Commission"  , "0x2ddf9ed285d762736917747694ed036851dfeaf4");
            Web3 web33 = new Web3("Advertise"   , "0x13a42739c1d18b49cd818aa8c4d6247a7f383487");

            web3Repository.saveAll(
                    List.of(  web31, web32, web33 )
            );
        };
    }
}
