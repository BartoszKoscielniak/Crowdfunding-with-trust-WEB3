package com.crowdfunding.crowdfundingapi.web3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3 {

    @Bean
    public Web3j getWeb3j(){
        return Web3j.build(new HttpService("https://eth-goerli.g.alchemy.com/v2/VTfA6Qb8FolAwH3n9o6e9l00J769A7gA"));
    }

}
