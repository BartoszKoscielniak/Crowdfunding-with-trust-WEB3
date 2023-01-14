package com.crowdfunding.crowdfundingapi.collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    @Query("SELECT c FROM Collection c WHERE c.id = ?1")
    Optional<Collection> findCollectionById(Long id);

    @Query("SELECT c FROM Collection c WHERE c.collectionName = ?1")
    Optional<Collection> findCollectionByName(String name);

    @Query("SELECT c FROM Collection c ORDER BY c.promoted DESC")
    List<Collection> findAllByPromo();
}
