package com.crowdfunding.crowdfundingapi.collection;

import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhase;
import com.crowdfunding.crowdfundingapi.support.CollUserRelation;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Collection {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String collectionName;
    @Column(nullable = false)
    private Double goal;
    @Column(nullable = false)
    private String baseDescription;
    @Column(nullable = false)
    private CollectionType collectionType;
    @Column(nullable = false)
    private Boolean promoted = false;
    @Column(nullable = false)
    private State state = State.NOT_PUBLISHED;
    private LocalDateTime promoTo;
    @Column(nullable = false)
    private Double actualFunds = 0.0;

    @JsonManagedReference
    @OneToMany(mappedBy = "collectionRelation", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(nullable = false)
    private List<CollUserRelation> collUserRelations;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Column(nullable = false)
    private List<CollectionPhase> collectionPhase;

    public Collection(Long id) {
        this.id = id;
    }

    public Collection(String collectionName, Double goal, String baseDescription, CollectionType collectionType, CollUserRelation... collUserRelations) {
        this.collectionName = collectionName;
        this.goal = goal;
        this.baseDescription = baseDescription;
        this.collectionType = collectionType;
        for(CollUserRelation collUserRelation : collUserRelations) collUserRelation.setCollectionRelation(this);
        this.collUserRelations = Stream.of(collUserRelations).collect(Collectors.toList());
        for(CollUserRelation collUserRelation : collUserRelations) collUserRelation.setCollectionRelation(this);
        this.collUserRelations = Stream.of(collUserRelations).collect(Collectors.toList());
    }
}
