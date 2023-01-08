package com.crowdfunding.crowdfundingapi.web3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface Web3Repository extends JpaRepository<Web3, Long> {

    @Query("SELECT w FROM Web3 w WHERE w.contractName = ?1")
    Optional<Web3> findContractByName(String contractName);

}
