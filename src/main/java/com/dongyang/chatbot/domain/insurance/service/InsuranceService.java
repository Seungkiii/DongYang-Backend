
package com.dongyang.chatbot.domain.insurance.service;

import org.springframework.stereotype.Service;

import com.dongyang.chatbot.domain.insurance.dto.InsuranceDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsuranceService {

    // This service contains the business logic for insurance-related features.
    // It's designed to be modular, so new services can be added for more complex features like recommendations.

    // This method retrieves basic information about a specific insurance product.
    // It currently returns a mock response, but in a real application, it would fetch data from a database.
    public InsuranceDto.InfoResponse getInsuranceInfo(String insuranceName) {
        // Mock data for demonstration purposes
        return new InsuranceDto.InfoResponse(
                "Critical Illness Insurance",
                "Provides a lump-sum payment if you're diagnosed with a serious illness (like cancer, heart attack, or stroke).",
                "- Diagnosis of a covered critical illness.\n- Survival period of 30 days after diagnosis.",
                "- Lump-sum payment of $100,000.\n- Coverage for 25 critical illnesses.",
                "- Pre-existing conditions.\n- Illnesses diagnosed within 90 days of policy start."
        );
    }

    // This method compares multiple insurance products based on their key features.
    // It currently returns a mock response, but in a real application, it would fetch data from a database and perform a comparison.
    public InsuranceDto.CompareResponse compareInsurances(InsuranceDto.CompareRequest request) {
        // Mock data for demonstration purposes
        List<InsuranceDto.InfoResponse> comparisonData = request.getInsuranceIds().stream()
                .map(name -> new InsuranceDto.InfoResponse(
                        name,
                        "Description for " + name,
                        "Conditions for " + name,
                        "Benefits for " + name,
                        "Exclusions for " + name
                ))
                .collect(Collectors.toList());

        return new InsuranceDto.CompareResponse(comparisonData, "Comparison summary for " + request.getComparisonType());
    }
}
