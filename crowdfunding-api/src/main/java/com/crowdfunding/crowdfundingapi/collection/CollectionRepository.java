package com.crowdfunding.crowdfundingapi.collection;

import com.crowdfunding.crowdfundingapi.collection.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    @Query("SELECT c FROM Collection c WHERE c.id = ?1")
    Optional<Collection> findCollectionById(Long id);
}
