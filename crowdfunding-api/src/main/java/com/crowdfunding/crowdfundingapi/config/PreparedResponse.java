package com.crowdfunding.crowdfundingapi.config;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@NoArgsConstructor
public class PreparedResponse {

    private Map<String, String> response;

    public Map<String, String> getSuccessResponse(String success){
        response.put("status", "success");
        response.put("result", success);

        return response;
    }

    public Map<String, String> getFailureResponse(String failure){
        response.put("status", "fail");
        response.put("error", failure);

        return response;
    }
}
