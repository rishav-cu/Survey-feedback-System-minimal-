package com.example.feedback.dto.response;

import java.util.Map;

public class QuestionAnalyticsResponse {
    private Long questionId;
    private String questionText;
    private String questionType;
    private Double averageRating;
    private Map<String, Long> answerCounts;

    public QuestionAnalyticsResponse() {}

    public QuestionAnalyticsResponse(Long questionId, String questionText, String questionType, Double averageRating, Map<String, Long> answerCounts) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.averageRating = averageRating;
        this.answerCounts = answerCounts;
    }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }
    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    public Map<String, Long> getAnswerCounts() { return answerCounts; }
    public void setAnswerCounts(Map<String, Long> answerCounts) { this.answerCounts = answerCounts; }
}
