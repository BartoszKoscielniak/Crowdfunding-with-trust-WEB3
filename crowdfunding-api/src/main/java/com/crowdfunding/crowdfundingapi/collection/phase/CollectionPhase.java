package com.crowdfunding.crowdfundingapi.collection.phase;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.poll.Poll;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Column(nullable = false)
    private Double goal;
    @Column(nullable = false)

    private Double actualFunds;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String phaseName;
    @Column(nullable = false)
    private LocalDateTime till;
    private String proofOfEvidence;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @JsonIgnore
    @OneToOne(mappedBy = "collectionPhase")
    private Poll poll;

    public CollectionPhase(Double goal, String description, String phaseName, Collection collection, LocalDateTime till, String proofOfEvidence) {
        this.goal = goal;
        this.phaseName = phaseName;
        this.description = description;
        this.collection = collection;
        this.actualFunds = 0.0;
        this.till = till;
        this.proofOfEvidence = proofOfEvidence;
    }
}
