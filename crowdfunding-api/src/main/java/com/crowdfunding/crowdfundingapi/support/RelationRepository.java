package com.crowdfunding.crowdfundingapi.support;

import com.crowdfunding.crowdfundingapi.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RelationRepository extends JpaRepository<CollUserRelation, Long> {

    @Query("SELECT r FROM CollUserRelation r WHERE r.user.id = ?1 AND r.collectionRelation.id = ?2")
    Optional<CollUserRelation> findRelationByUseridCollectionid(Long userId, Long collectionId);

    @Query("SELECT r FROM CollUserRelation r WHERE r.user.id = ?1")
    List<CollUserRelation> findRelationByUserid(Long userId);

    @Query("SELECT r FROM CollUserRelation r WHERE r.collectionRelation.id = ?1")
    List<CollUserRelation> findRelationByCollectionid(Long collectionId);
}
