package com.crowdfunding.crowdfundingapi.collection;

import com.crowdfunding.crowdfundingapi.collection.phase.CollectionPhase;
import com.crowdfunding.crowdfundingapi.support.CollUserRelation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Collection {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id", nullable = false)
    private Long id;
    private Double goal;
    private String baseDescription;
    private CollectionType collectionType;
    @JsonIgnore
    @OneToMany(mappedBy = "collectionRelation", cascade = CascadeType.ALL)
    private Set<CollUserRelation> collUserRelations;

    @OneToMany(mappedBy = "collection")
    private Set<CollectionPhase> collectionPhase;

    public Collection(Double goal, String baseDescription, CollectionType collectionType, CollUserRelation... collUserRelations) {
        this.goal = goal;
        this.baseDescription = baseDescription;
        this.collectionType = collectionType;
        for(CollUserRelation collUserRelation : collUserRelations) collUserRelation.setCollectionRelation(this);//TODO:change?
        this.collUserRelations = Stream.of(collUserRelations).collect(Collectors.toSet());
    }
}
