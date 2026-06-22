package com.example.feedback.dto.response;

import java.util.List;

public class AnalyticsResponse {
    private Long surveyId;
    private Integer totalResponses;
    private Double averageRating;
    private List<QuestionAnalyticsResponse> questionAnalytics;

    public AnalyticsResponse() {}

    public AnalyticsResponse(Long surveyId, Integer totalResponses, Double averageRating, List<QuestionAnalyticsResponse> questionAnalytics) {
        this.surveyId = surveyId;
        this.totalResponses = totalResponses;
        this.averageRating = averageRating;
        this.questionAnalytics = questionAnalytics;
    }

    public Long getSurveyId() { return surveyId; }
    public void setSurveyId(Long surveyId) { this.surveyId = surveyId; }
    public Integer getTotalResponses() { return totalResponses; }
    public void setTotalResponses(Integer totalResponses) { this.totalResponses = totalResponses; }
    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    public List<QuestionAnalyticsResponse> getQuestionAnalytics() { return questionAnalytics; }
    public void setQuestionAnalytics(List<QuestionAnalyticsResponse> questionAnalytics) { this.questionAnalytics = questionAnalytics; }
}
