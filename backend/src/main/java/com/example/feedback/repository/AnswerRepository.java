package com.example.feedback.repository;

import com.example.feedback.model.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("SELECT AVG(a.ratingValue) FROM Answer a WHERE a.question.survey.id = :surveyId AND a.question.questionType = 'RATING'")
    Double getAverageRatingForSurvey(@Param("surveyId") Long surveyId);
}
