package com.crowdfunding.crowdfundingapi.config;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@NoArgsConstructor
public class PreparedResponse {

    private Map<String, String> response = new HashMap<>();

    public Map<String, String> getSuccessResponse(String success){
        this.response.put("status", "success");
        this.response.put("result", success);

        return this.response;
    }

    public Map<String, String> getFailureResponse(String failure){
        response.put("status", "fail");
        response.put("error", failure);

        return response;
    }
}
