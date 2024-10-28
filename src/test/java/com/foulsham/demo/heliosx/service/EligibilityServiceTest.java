package com.foulsham.demo.heliosx.service;

import static org.junit.jupiter.api.Assertions.*;

import com.foulsham.demo.heliosx.dto.consultation.BooleanAnswer;
import com.foulsham.demo.heliosx.dto.consultation.ConsultationSubmissionResponse;
import com.foulsham.demo.heliosx.model.consultation.BooleanQuestion;
import com.foulsham.demo.heliosx.model.consultation.Consultation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

class EligibilityServiceTest {

  private EligibilityService eligibilityService;

  @BeforeEach
  void setUp() {
    eligibilityService = new EligibilityService();
  }

  @Test
  void testEvaluateEligibility_IncompleteAnswers() {
    UUID questionId = UUID.randomUUID();
    BooleanQuestion question = new BooleanQuestion(questionId, "Is this a test question?", true);
    Consultation consultation =
        new Consultation(
            UUID.randomUUID(), "ref-123", "Test Consultation", Collections.singletonList(question));

    List<BooleanAnswer> answers = Collections.emptyList();

    ConsultationSubmissionResponse response =
        eligibilityService.evaluateEligibility(consultation, answers);

    assertFalse(response.likelyToPrescribe());
    assertEquals("Please answer all questions.", response.message());
  }

  @Test
  void testEvaluateEligibility_AllCorrectAnswers() {
    UUID questionId = UUID.randomUUID();
    BooleanQuestion question = new BooleanQuestion(questionId, "Is this a test question?", true);
    Consultation consultation =
        new Consultation(
            UUID.randomUUID(), "ref-123", "Test Consultation", Collections.singletonList(question));

    BooleanAnswer answer = new BooleanAnswer(questionId, true);
    List<BooleanAnswer> answers = Collections.singletonList(answer);

    ConsultationSubmissionResponse response =
        eligibilityService.evaluateEligibility(consultation, answers);

    assertTrue(response.likelyToPrescribe());
    assertEquals(
        "Your prescription is likely to be successful. We will be in touch shortly.",
        response.message());
  }

  @Test
  void testEvaluateEligibility_IncorrectAnswers() {
    UUID questionId = UUID.randomUUID();
    BooleanQuestion question = new BooleanQuestion(questionId, "Is this a test question?", true);
    Consultation consultation =
        new Consultation(
            UUID.randomUUID(), "ref-123", "Test Consultation", Collections.singletonList(question));

    BooleanAnswer answer = new BooleanAnswer(questionId, false);
    List<BooleanAnswer> answers = Collections.singletonList(answer);

    ConsultationSubmissionResponse response =
        eligibilityService.evaluateEligibility(consultation, answers);

    assertFalse(response.likelyToPrescribe());
    assertEquals(
        "Your prescription is unlikely to be successful. We will be in touch shortly.",
        response.message());
  }

  @Test
  void testEvaluateEligibility_MultipleQuestions_MixedAnswers() {
    UUID questionId1 = UUID.randomUUID();
    UUID questionId2 = UUID.randomUUID();
    BooleanQuestion question1 = new BooleanQuestion(questionId1, "First question?", true);
    BooleanQuestion question2 = new BooleanQuestion(questionId2, "Second question?", false);
    Consultation consultation =
        new Consultation(
            UUID.randomUUID(), "ref-123", "Test Consultation", List.of(question1, question2));

    BooleanAnswer answer1 = new BooleanAnswer(questionId1, true);
    BooleanAnswer answer2 = new BooleanAnswer(questionId2, false);
    List<BooleanAnswer> answers = List.of(answer1, answer2);

    ConsultationSubmissionResponse response =
        eligibilityService.evaluateEligibility(consultation, answers);

    assertTrue(response.likelyToPrescribe());
    assertEquals(
        "Your prescription is likely to be successful. We will be in touch shortly.",
        response.message());
  }
}
