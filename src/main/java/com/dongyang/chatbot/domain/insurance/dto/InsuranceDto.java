
package com.dongyang.chatbot.domain.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.List;

public class InsuranceDto {

    // This class defines the Data Transfer Objects (DTOs) for insurance-related features.
    // DTOs are used to transfer data between the client and server in a structured way.

    @Getter
    @AllArgsConstructor
    public static class InfoResponse {
        private String name;
        private String description;
        private String conditions;
        private String benefits;
        private String exclusions;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompareRequest {
        private List<String> insuranceIds;
        private String comparisonType;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompareResponse {
        private List<InsuranceDto.InfoResponse> comparisons;
        private String summary;
    }
}
