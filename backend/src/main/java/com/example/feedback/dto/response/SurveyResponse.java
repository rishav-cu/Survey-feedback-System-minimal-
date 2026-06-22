package com.example.feedback.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class SurveyResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private String status;
    private List<QuestionResponse> questions;

    public SurveyResponse() {}

    public SurveyResponse(Long id, String title, String description, LocalDateTime createdAt, String status, List<QuestionResponse> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.status = status;
        this.questions = questions;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<QuestionResponse> getQuestions() { return questions; }
    public void setQuestions(List<QuestionResponse> questions) { this.questions = questions; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String title;
        private String description;
        private LocalDateTime createdAt;
        private String status;
        private List<QuestionResponse> questions;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder questions(List<QuestionResponse> questions) { this.questions = questions; return this; }
        public SurveyResponse build() { return new SurveyResponse(id, title, description, createdAt, status, questions); }
    }

    public static class QuestionResponse {
        private Long id;
        private String questionText;
        private String questionType;
        private List<String> options;

        public QuestionResponse() {}

        public QuestionResponse(Long id, String questionText, String questionType, List<String> options) {
            this.id = id;
            this.questionText = questionText;
            this.questionType = questionType;
            this.options = options;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getQuestionText() { return questionText; }
        public void setQuestionText(String questionText) { this.questionText = questionText; }
        public String getQuestionType() { return questionType; }
        public void setQuestionType(String questionType) { this.questionType = questionType; }
        public List<String> getOptions() { return options; }
        public void setOptions(List<String> options) { this.options = options; }
    }
}
