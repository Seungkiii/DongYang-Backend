
package com.dongyang.chatbot.domain.insurance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongyang.chatbot.domain.insurance.dto.InsuranceDto;
import com.dongyang.chatbot.domain.insurance.service.InsuranceService;

@RestController
@RequestMapping("/api/v1/insurances")
public class InsuranceController {

    // This controller handles API endpoints for insurance-related features.
    // It's designed to be modular, allowing for easy expansion of new features.

    private final InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    // This endpoint retrieves basic information about a specific insurance product.
    // It takes the insurance name as a path variable and returns the details.
    @GetMapping("/{insuranceName}")
    public InsuranceDto.InfoResponse getInsuranceInfo(@PathVariable String insuranceName) {
        return insuranceService.getInsuranceInfo(insuranceName);
    }

    // This endpoint compares multiple insurance products based on their key features.
    // It takes a list of insurance names in the request body and returns a comparison table.
    @PostMapping("/compare")
    public InsuranceDto.CompareResponse compareInsurances(@RequestBody InsuranceDto.CompareRequest request) {
        return insuranceService.compareInsurances(request);
    }
}
