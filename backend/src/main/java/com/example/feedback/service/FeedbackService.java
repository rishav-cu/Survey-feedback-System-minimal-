package com.example.feedback.service;

import com.example.feedback.dto.request.AnswerRequest;
import com.example.feedback.dto.request.FeedbackSubmitRequest;
import com.example.feedback.exception.ResourceNotFoundException;
import com.example.feedback.model.entity.Answer;
import com.example.feedback.model.entity.FeedbackResponse;
import com.example.feedback.model.entity.Question;
import com.example.feedback.model.entity.Survey;
import com.example.feedback.model.entity.User;
import com.example.feedback.model.enums.QuestionType;
import com.example.feedback.repository.FeedbackResponseRepository;
import com.example.feedback.repository.QuestionRepository;
import com.example.feedback.repository.SurveyRepository;
import com.example.feedback.repository.UserRepository;
import com.example.feedback.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private final FeedbackResponseRepository feedbackResponseRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public FeedbackService(FeedbackResponseRepository feedbackResponseRepository, SurveyRepository surveyRepository, QuestionRepository questionRepository, UserRepository userRepository) {
        this.feedbackResponseRepository = feedbackResponseRepository;
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void submitFeedback(FeedbackSubmitRequest request, String username) {
        User respondent = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Survey survey = surveyRepository.findById(request.getSurveyId())
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        if (survey.getStatus() != com.example.feedback.model.enums.SurveyStatus.ACTIVE) {
            throw new IllegalArgumentException("This survey is no longer active");
        }

        FeedbackResponse feedbackResponse = FeedbackResponse.builder()
                .survey(survey)
                .respondent(respondent)
                .build();

        Map<Long, Question> questionMap = survey.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        List<Answer> answers = new ArrayList<>();

        for (AnswerRequest answerReq : request.getAnswers()) {
            Question question = questionMap.get(answerReq.getQuestionId());
            if (question == null) {
                throw new ResourceNotFoundException("Question with ID " + answerReq.getQuestionId() + " does not belong to this survey");
            }

            validateAnswer(answerReq, question);

            Answer answer = Answer.builder()
                    .feedbackResponse(feedbackResponse)
                    .question(question)
                    .answerText(answerReq.getAnswerText())
                    .ratingValue(answerReq.getRatingValue())
                    .build();
            
            answers.add(answer);
        }

        feedbackResponse.setAnswers(answers);
        feedbackResponseRepository.save(feedbackResponse);
    }

    private void validateAnswer(AnswerRequest answerReq, Question question) {
        if (question.getQuestionType() == QuestionType.RATING) {
            if (answerReq.getRatingValue() == null || answerReq.getRatingValue() < 1 || answerReq.getRatingValue() > 5) {
                throw new IllegalArgumentException("Rating for question " + question.getId() + " must be between 1 and 5");
            }
        } else if (question.getQuestionType() == QuestionType.TEXT || question.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
            if (answerReq.getAnswerText() == null || answerReq.getAnswerText().trim().isEmpty()) {
                throw new IllegalArgumentException("Text answer for question " + question.getId() + " cannot be empty");
            }
        }
    }
}
