package com.example.feedback.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_response_id", nullable = false)
    private FeedbackResponse feedbackResponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(length = 1000)
    private String answerText;

    private Integer ratingValue;

    public Answer() {}

    public Answer(Long id, FeedbackResponse feedbackResponse, Question question, String answerText, Integer ratingValue) {
        this.id = id;
        this.feedbackResponse = feedbackResponse;
        this.question = question;
        this.answerText = answerText;
        this.ratingValue = ratingValue;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public FeedbackResponse getFeedbackResponse() { return feedbackResponse; }
    public void setFeedbackResponse(FeedbackResponse feedbackResponse) { this.feedbackResponse = feedbackResponse; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
    public Integer getRatingValue() { return ratingValue; }
    public void setRatingValue(Integer ratingValue) { this.ratingValue = ratingValue; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private FeedbackResponse feedbackResponse;
        private Question question;
        private String answerText;
        private Integer ratingValue;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder feedbackResponse(FeedbackResponse feedbackResponse) { this.feedbackResponse = feedbackResponse; return this; }
        public Builder question(Question question) { this.question = question; return this; }
        public Builder answerText(String answerText) { this.answerText = answerText; return this; }
        public Builder ratingValue(Integer ratingValue) { this.ratingValue = ratingValue; return this; }
        public Answer build() { return new Answer(id, feedbackResponse, question, answerText, ratingValue); }
    }
}
