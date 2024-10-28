package com.foulsham.demo.heliosx.service;

import com.foulsham.demo.heliosx.dto.consultation.BooleanAnswer;
import com.foulsham.demo.heliosx.dto.consultation.ConsultationSubmissionResponse;
import com.foulsham.demo.heliosx.model.consultation.BooleanQuestion;
import com.foulsham.demo.heliosx.model.consultation.Consultation;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class EligibilityService {

  public ConsultationSubmissionResponse evaluateEligibility(
      Consultation consultation, List<BooleanAnswer> answers) {
    List<BooleanQuestion> questions = consultation.questions();

    if (answers.size() != questions.size()) {
      return new ConsultationSubmissionResponse(false, "Please answer all questions.");
    }

    // Map each question ID to its required answer for validation
    Map<UUID, Boolean> requiredAnswers =
        questions.stream()
            .collect(Collectors.toMap(BooleanQuestion::id, BooleanQuestion::requiredAnswer));

    // Check if all answers match the required answers and all questions have corresponding answers
    boolean isEligible =
        answers.stream()
            .allMatch(
                answer ->
                    requiredAnswers.containsKey(answer.questionId())
                        && requiredAnswers.get(answer.questionId()).equals(answer.answer()));

    String message =
        isEligible
            ? "Your prescription is likely to be successful. We will be in touch shortly."
            : "Your prescription is unlikely to be successful. We will be in touch shortly.";

    return new ConsultationSubmissionResponse(isEligible, message);
  }
}
