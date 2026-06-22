package com.example.feedback.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class FeedbackSubmitRequest {

    @NotNull(message = "Survey ID is required")
    private Long surveyId;

    @NotEmpty(message = "Answers cannot be empty")
    @Valid
    private List<AnswerRequest> answers;

    public FeedbackSubmitRequest() {}

    public Long getSurveyId() { return surveyId; }
    public void setSurveyId(Long surveyId) { this.surveyId = surveyId; }
    public List<AnswerRequest> getAnswers() { return answers; }
    public void setAnswers(List<AnswerRequest> answers) { this.answers = answers; }
}
