package com.crowdfunding.crowdfundingapi.web3;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    public Web3(String contractName, String contractAddress) {
        this.contractName = contractName;
        this.contractAddress = contractAddress;
    }

    @Bean
    public Web3j getWeb3j(){
        return Web3j.build(new HttpService("https://eth-goerli.g.alchemy.com/v2/VTfA6Qb8FolAwH3n9o6e9l00J769A7gA"));
    }

}
