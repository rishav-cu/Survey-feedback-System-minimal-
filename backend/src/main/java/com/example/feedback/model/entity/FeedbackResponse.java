package com.example.feedback.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "feedback_responses")
public class FeedbackResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondent_id", nullable = false)
    private User respondent;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "feedbackResponse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public FeedbackResponse() {}

    public FeedbackResponse(Long id, Survey survey, User respondent, LocalDateTime submittedAt, List<Answer> answers) {
        this.id = id;
        this.survey = survey;
        this.respondent = respondent;
        this.submittedAt = submittedAt;
        this.answers = answers != null ? answers : new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Survey getSurvey() { return survey; }
    public void setSurvey(Survey survey) { this.survey = survey; }
    public User getRespondent() { return respondent; }
    public void setRespondent(User respondent) { this.respondent = respondent; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public List<Answer> getAnswers() { return answers; }
    public void setAnswers(List<Answer> answers) { this.answers = answers; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Survey survey;
        private User respondent;
        private LocalDateTime submittedAt;
        private List<Answer> answers = new ArrayList<>();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder survey(Survey survey) { this.survey = survey; return this; }
        public Builder respondent(User respondent) { this.respondent = respondent; return this; }
        public Builder submittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; return this; }
        public Builder answers(List<Answer> answers) { this.answers = answers; return this; }
        public FeedbackResponse build() { return new FeedbackResponse(id, survey, respondent, submittedAt, answers); }
    }
}
