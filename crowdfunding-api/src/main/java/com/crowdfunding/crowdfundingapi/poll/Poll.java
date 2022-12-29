package com.crowdfunding.crowdfundingapi.poll;

import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhase;
import com.crowdfunding.crowdfundingapi.poll.vote.Vote;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int allowedUsersCount = 0;

    private PollState state;
    private LocalDate startDate;

    @OneToMany(mappedBy = "poll")
    private Set<Vote> votes;

    @OneToOne
    @JoinColumn(name = "phase_id")
    private CollectionPhase collectionPhase;

    public Poll(PollState state, CollectionPhase collectionPhase) {
        this.state = state;
        this.collectionPhase = collectionPhase;
    }

    public Poll(PollState state) {
        this.state = state;
    }
}
