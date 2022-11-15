package com.crowdfunding.crowdfundingapi.config.security;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthenticationRequest {

    private String publicaddress;
    private String password;
    private String signature;

    public String getSignature( ) {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPublicaddress( ) {
        return publicaddress;
    }

    public void setPublicaddress(String publicaddress) {
        this.publicaddress = publicaddress;
    }

    public String getPassword( ) {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}