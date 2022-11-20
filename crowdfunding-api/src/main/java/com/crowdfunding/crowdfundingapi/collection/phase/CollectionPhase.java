package com.crowdfunding.crowdfundingapi.collection.phase;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.poll.Poll;
import com.crowdfunding.crowdfundingapi.poll.vote.Vote;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class CollectionPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phase_id", nullable = false)
    private Long id;
    private Double goal;
    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @JsonIgnore
    @OneToOne(mappedBy = "collectionPhase")
    private Poll poll;

    public CollectionPhase(Double goal, String description, Collection collection, Poll poll) {
        this.goal = goal;
        this.description = description;
        this.collection = collection;
        this.poll = poll;
    }

    public CollectionPhase(Double goal, String description, Collection collection) {
        this.goal = goal;
        this.description = description;
        this.collection = collection;
    }
}
