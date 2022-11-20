package com.crowdfunding.crowdfundingapi.support;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import com.crowdfunding.crowdfundingapi.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class CollUserRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "collection_id")
    private Collection collectionRelation;
    private CollUserType type;

    public CollUserRelation(User user, CollUserType type) {
        this.user = user;
        this.type = type;
    }

    public CollUserRelation(User user, Collection collectionRelation, CollUserType type) {
        this.user = user;
        this.collectionRelation = collectionRelation;
        this.type = type;
    }

    @Override
    public int hashCode( ) {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


}
