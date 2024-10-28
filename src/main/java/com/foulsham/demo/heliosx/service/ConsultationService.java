package com.foulsham.demo.heliosx.service;

import com.foulsham.demo.heliosx.dto.consultation.ConsultationDto;
import com.foulsham.demo.heliosx.dto.consultation.ConsultationSubmissionRequest;
import com.foulsham.demo.heliosx.dto.consultation.ConsultationSubmissionResponse;
import com.foulsham.demo.heliosx.mapper.ConsultationMapper;
import com.foulsham.demo.heliosx.model.consultation.Consultation;
import com.foulsham.demo.heliosx.repository.MockConsultationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ConsultationService {

  public static Logger log = LoggerFactory.getLogger(ConsultationService.class);

  private final MockConsultationRepository consultationRepository;
  private final ConsultationMapper consultationMapper;
  private final EligibilityService eligibilityService;

  public ConsultationService(
      MockConsultationRepository consultationRepository,
      ConsultationMapper consultationMapper,
      EligibilityService eligibilityService) {
    this.consultationRepository = consultationRepository;
    this.consultationMapper = consultationMapper;
    this.eligibilityService = eligibilityService;
  }

  public ConsultationDto getConsultationByReference(String reference) {
    return consultationRepository
        .getConsultationByReference(reference)
        .map(consultationMapper::toDto)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation not found"));
  }

  public ConsultationSubmissionResponse submitConsultation(ConsultationSubmissionRequest request) {
    log.debug("Submitting response for: {}", request.id());
    Consultation consultation =
        consultationRepository
            .getConsultationById(request.id())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation not found"));

    consultationRepository.saveConsultationSubmission(request);
    return eligibilityService.evaluateEligibility(consultation, request.answers());
  }
}
