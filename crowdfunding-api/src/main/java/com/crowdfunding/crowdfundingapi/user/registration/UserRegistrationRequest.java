package com.crowdfunding.crowdfundingapi.user.registration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@NoArgsConstructor
public class UserRegistrationRequest {

    private String publicAddress;
    private String nonce;
}
