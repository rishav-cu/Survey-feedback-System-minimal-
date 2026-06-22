package com.example.feedback.controller;

import com.example.feedback.dto.request.SurveyCreateRequest;
import com.example.feedback.dto.request.SurveyStatusRequest;
import com.example.feedback.dto.response.AnalyticsResponse;
import com.example.feedback.dto.response.SurveyResponse;
import com.example.feedback.service.SurveyService;
import jakarta.validation.Valid;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping
    public ResponseEntity<SurveyResponse> createSurvey(
            @Valid @RequestBody SurveyCreateRequest request,
            Authentication authentication) {
        return new ResponseEntity<>(surveyService.createSurvey(request, authentication.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyAnalytics(id));
    }

    @GetMapping
    public ResponseEntity<List<SurveyResponse>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyResponse> getSurveyById(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SurveyResponse> updateSurveyStatus(
            @PathVariable Long id,
            @Valid @RequestBody SurveyStatusRequest request) {
        return ResponseEntity.ok(surveyService.updateSurveyStatus(id, request.getStatus()));
    }
}
