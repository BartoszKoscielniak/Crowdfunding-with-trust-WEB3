package com.crowdfunding.crowdfundingapi.poll;

import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhase;
import com.crowdfunding.crowdfundingapi.poll.vote.Vote;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    private int allowedUsersCount = 1;

    private PollState state;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "poll", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Vote> votes;

    @OneToOne
    @JoinColumn(name = "phase_id")
    private CollectionPhase collectionPhase;

    public Poll(PollState state, CollectionPhase collectionPhase, LocalDateTime startDate) {
        this.state = state;
        this.collectionPhase = collectionPhase;
        this.startDate = startDate;
        this.endDate = startDate.plusDays(1);
    }

    public Poll(PollState state) {
        this.state = state;
    }
}
