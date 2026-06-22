package com.example.feedback.repository;

import com.example.feedback.model.entity.FeedbackResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackResponseRepository extends JpaRepository<FeedbackResponse, Long> {
    java.util.List<FeedbackResponse> findBySurveyId(Long surveyId);
}
