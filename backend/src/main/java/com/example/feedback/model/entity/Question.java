package com.example.feedback.model.entity;

import com.example.feedback.model.enums.QuestionType;
import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Column(nullable = false)
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;

    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text")
    private java.util.List<String> options;

    public Question() {}

    public Question(Long id, Survey survey, String questionText, QuestionType questionType, java.util.List<String> options) {
        this.id = id;
        this.survey = survey;
        this.questionText = questionText;
        this.questionType = questionType;
        this.options = options;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Survey getSurvey() { return survey; }
    public void setSurvey(Survey survey) { this.survey = survey; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public QuestionType getQuestionType() { return questionType; }
    public void setQuestionType(QuestionType questionType) { this.questionType = questionType; }
    public java.util.List<String> getOptions() { return options; }
    public void setOptions(java.util.List<String> options) { this.options = options; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Survey survey;
        private String questionText;
        private QuestionType questionType;
        private java.util.List<String> options;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder survey(Survey survey) { this.survey = survey; return this; }
        public Builder questionText(String questionText) { this.questionText = questionText; return this; }
        public Builder questionType(QuestionType questionType) { this.questionType = questionType; return this; }
        public Builder options(java.util.List<String> options) { this.options = options; return this; }
        public Question build() { return new Question(id, survey, questionText, questionType, options); }
    }
}
