package com.example.feedback.service;

import com.example.feedback.dto.request.SurveyCreateRequest;
import com.example.feedback.dto.response.AnalyticsResponse;
import com.example.feedback.dto.response.QuestionAnalyticsResponse;
import com.example.feedback.dto.response.SurveyResponse;
import com.example.feedback.exception.ResourceNotFoundException;
import com.example.feedback.model.entity.*;
import com.example.feedback.model.enums.QuestionType;
import com.example.feedback.model.enums.SurveyStatus;
import com.example.feedback.repository.AnswerRepository;
import com.example.feedback.repository.FeedbackResponseRepository;
import com.example.feedback.repository.SurveyRepository;
import com.example.feedback.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final FeedbackResponseRepository feedbackResponseRepository;

    public SurveyService(SurveyRepository surveyRepository, UserRepository userRepository, AnswerRepository answerRepository, FeedbackResponseRepository feedbackResponseRepository) {
        this.surveyRepository = surveyRepository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.feedbackResponseRepository = feedbackResponseRepository;
    }

    @Transactional
    public SurveyResponse createSurvey(SurveyCreateRequest request, String username) {
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Survey survey = Survey.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .creator(creator)
                .status(SurveyStatus.DRAFT)
                .build();

        List<Question> questions = request.getQuestions().stream()
                .map(qReq -> Question.builder()
                        .survey(survey)
                        .questionText(qReq.getQuestionText())
                        .questionType(qReq.getQuestionType())
                        .options(qReq.getOptions())
                        .build())
                .collect(Collectors.toList());

        survey.setQuestions(questions);

        Survey savedSurvey = surveyRepository.save(survey);
        return mapToSurveyResponse(savedSurvey);
    }

    @Transactional
    public SurveyResponse updateSurveyStatus(Long surveyId, String status) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));
        survey.setStatus(SurveyStatus.valueOf(status.toUpperCase()));
        return mapToSurveyResponse(surveyRepository.save(survey));
    }

    public AnalyticsResponse getSurveyAnalytics(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with ID: " + surveyId));

        List<FeedbackResponse> responses = feedbackResponseRepository.findBySurveyId(surveyId);
        int totalResponses = responses.size();

        Double overallAverage = answerRepository.getAverageRatingForSurvey(surveyId);

        List<QuestionAnalyticsResponse> questionAnalytics = survey.getQuestions().stream().map(q -> {
            QuestionAnalyticsResponse qa = new QuestionAnalyticsResponse();
            qa.setQuestionId(q.getId());
            qa.setQuestionText(q.getQuestionText());
            qa.setQuestionType(q.getQuestionType().name());

            if (q.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
                java.util.Map<String, Long> counts = new java.util.HashMap<>();
                if (q.getOptions() != null) {
                    q.getOptions().forEach(opt -> counts.put(opt, 0L));
                }
                responses.forEach(r -> {
                    r.getAnswers().stream()
                            .filter(a -> a.getQuestion().getId().equals(q.getId()) && a.getAnswerText() != null)
                            .forEach(a -> counts.put(a.getAnswerText(), counts.getOrDefault(a.getAnswerText(), 0L) + 1));
                });
                qa.setAnswerCounts(counts);
            } else if (q.getQuestionType() == QuestionType.RATING) {
                double avg = responses.stream()
                        .flatMap(r -> r.getAnswers().stream())
                        .filter(a -> a.getQuestion().getId().equals(q.getId()) && a.getRatingValue() != null)
                        .mapToInt(Answer::getRatingValue)
                        .average().orElse(0.0);
                qa.setAverageRating(Math.round(avg * 10.0) / 10.0);
            }
            return qa;
        }).collect(Collectors.toList());

        return new AnalyticsResponse(surveyId, totalResponses, overallAverage != null ? overallAverage : 0.0, questionAnalytics);
    }

    public List<SurveyResponse> getAllSurveys() {
        return surveyRepository.findAll().stream()
                .map(this::mapToSurveyResponse)
                .collect(Collectors.toList());
    }

    public SurveyResponse getSurveyById(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with ID: " + surveyId));
        return mapToSurveyResponse(survey);
    }

    private SurveyResponse mapToSurveyResponse(Survey survey) {
        List<SurveyResponse.QuestionResponse> questionResponses = survey.getQuestions().stream()
                .map(q -> new SurveyResponse.QuestionResponse(q.getId(), q.getQuestionText(), q.getQuestionType().name(), q.getOptions()))
                .collect(Collectors.toList());

        return SurveyResponse.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .createdAt(survey.getCreatedAt())
                .status(survey.getStatus().name())
                .questions(questionResponses)
                .build();
    }
}
