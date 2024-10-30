package com.foulsham.demo.heliosx.repository;

import com.foulsham.demo.heliosx.dto.consultation.ConsultationSubmissionRequest;
import com.foulsham.demo.heliosx.exception.ConsultationSubmissionAlreadyExistsException;
import com.foulsham.demo.heliosx.model.consultation.BooleanQuestion;
import com.foulsham.demo.heliosx.model.consultation.Consultation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/** Mock Consultation Repository`in place of a real database. */
@Service
public class MockConsultationRepository {

  public static Logger log = LoggerFactory.getLogger(MockConsultationRepository.class);

  private final List<ConsultationSubmissionRequest> consultationSubmissions = new ArrayList<>();

  public Optional<Consultation> getConsultationByReference(String reference) {
    if (reference.equals("genovian-pear")) {
      return Optional.of(getPearConsultation());
    }
    return Optional.empty();
  }

  public Optional<Consultation> getConsultationById(UUID id) {
    if (id.equals(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"))) {
      return Optional.of(getPearConsultation());
    }
    return Optional.empty();
  }

  public void saveConsultationSubmission(ConsultationSubmissionRequest request) {
    // Check for existing submission based on ID and email
    boolean exists =
        consultationSubmissions.stream()
            .anyMatch(
                submission ->
                    submission.id().equals(request.id())
                        && submission.email().equals(request.email()));

    if (exists) {
      log.info("Consultation submission already exists for: {}", request.id());
      throw new ConsultationSubmissionAlreadyExistsException(
          "Consultation submission already exists");
    }
    consultationSubmissions.add(request);
  }

  public Consultation getPearConsultation() {
    BooleanQuestion question0 =
        new BooleanQuestion(
            UUID.fromString("d2d08c24-54af-4430-a584-15055ce2babf"),
            "Are you aged 18-90 years old?",
            true);
    BooleanQuestion question1 =
        new BooleanQuestion(
            UUID.fromString("488b98ba-553d-43f7-ba09-f7bbbb3cfba9"),
            "Have you experienced a rash when eating pears?",
            true);
    BooleanQuestion question2 =
        new BooleanQuestion(
            UUID.fromString("842fe0aa-4f94-4e1c-8a1f-cf60736d099f"),
            "Have you experienced swelling of the throat when looking at pears?",
            false);
    return new Consultation(
        UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"),
        "genovian-pear",
        "Genovian Pear Consultation",
        List.of(question0, question1, question2));
  }
}
