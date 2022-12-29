package com.crowdfunding.crowdfundingapi.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.publicAddress = ?1")
    Optional<User> findUserByPublicAddress(String publicAddress);

    @Query("SELECT u FROM User u WHERE u.phoneNumber = ?1 OR u.email = ?2 OR u.publicAddress = ?3")
    Optional<User> findUserByPhoneNumberOrEmailOrPublicAddress(String phoneNumber, String email, String publicAddress);

    @Query("SELECT u FROM User u WHERE u.id = ?1")
    Optional<User> findUserById(Long id);

}