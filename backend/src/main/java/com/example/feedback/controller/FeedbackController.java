package com.example.feedback.controller;

import com.example.feedback.dto.request.FeedbackSubmitRequest;
import com.example.feedback.service.FeedbackService;
import jakarta.validation.Valid;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> submitFeedback(
            @Valid @RequestBody FeedbackSubmitRequest request,
            Authentication authentication) {
        feedbackService.submitFeedback(request, authentication.getName());
        return new ResponseEntity<>(Map.of("message", "Feedback submitted successfully"), HttpStatus.CREATED);
    }
}
