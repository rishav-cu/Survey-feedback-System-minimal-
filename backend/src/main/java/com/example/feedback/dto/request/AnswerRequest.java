package com.example.feedback.dto.request;

import jakarta.validation.constraints.NotNull;

public class AnswerRequest {

    @NotNull(message = "Question ID is required")
    private Long questionId;

    private String answerText;

    private Integer ratingValue;

    public AnswerRequest() {}

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
    public Integer getRatingValue() { return ratingValue; }
    public void setRatingValue(Integer ratingValue) { this.ratingValue = ratingValue; }
}
