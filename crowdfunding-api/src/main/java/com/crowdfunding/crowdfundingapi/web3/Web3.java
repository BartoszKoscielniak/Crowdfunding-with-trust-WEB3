package com.crowdfunding.crowdfundingapi.web3;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import javax.persistence.*;


@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Web3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contractName;
    private String contractAddress;
    @Transient
    @Value("${crowdfunding.blockchain.http}")
    private String nodeAddress;

    public Web3(String contractName, String contractAddress) {
        this.contractName = contractName;
        this.contractAddress = contractAddress;
    }

    @Bean
    public Web3j getWeb3j(){
        return Web3j.build(new HttpService(nodeAddress));
    }

}
