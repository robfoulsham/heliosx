package com.foulsham.demo.heliosx.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.foulsham.demo.heliosx.dto.consultation.ConsultationDto;
import com.foulsham.demo.heliosx.dto.consultation.ConsultationSubmissionRequest;
import com.foulsham.demo.heliosx.dto.consultation.ConsultationSubmissionResponse;
import com.foulsham.demo.heliosx.mapper.ConsultationMapper;
import com.foulsham.demo.heliosx.model.consultation.Consultation;
import com.foulsham.demo.heliosx.repository.MockConsultationRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

class ConsultationServiceTest {

  @Mock private MockConsultationRepository consultationRepository;
  @Mock private ConsultationMapper consultationMapper;
  @Mock private EligibilityService eligibilityService;

  @InjectMocks private ConsultationService consultationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetConsultationByReference_Success() {
    // Arrange
    String reference = "test-reference";
    Consultation consultation =
        new Consultation(
            UUID.randomUUID(), reference, "Test Consultation", Collections.emptyList());
    ConsultationDto consultationDto =
        new ConsultationDto(
            consultation.id(),
            consultation.reference(),
            consultation.name(),
            Collections.emptyList());

    when(consultationRepository.getConsultationByReference(reference))
        .thenReturn(Optional.of(consultation));
    when(consultationMapper.toDto(consultation)).thenReturn(consultationDto);

    // Act
    ConsultationDto result = consultationService.getConsultationByReference(reference);

    // Assert
    assertNotNull(result);
    assertEquals(reference, result.reference());
    verify(consultationRepository).getConsultationByReference(reference);
    verify(consultationMapper).toDto(consultation);
  }

  @Test
  void testGetConsultationByReference_NotFound() {
    // Arrange
    String reference = "non-existent-reference";
    when(consultationRepository.getConsultationByReference(reference)).thenReturn(Optional.empty());

    // Act & Assert
    ResponseStatusException exception =
        assertThrows(
            ResponseStatusException.class,
            () -> consultationService.getConsultationByReference(reference));
    assertEquals("404 NOT_FOUND \"Consultation not found\"", exception.getMessage());
    verify(consultationRepository).getConsultationByReference(reference);
    verify(consultationMapper, never()).toDto(any());
  }

  @Test
  void testSubmitConsultation_Success() {
    UUID consultationId = UUID.randomUUID();
    ConsultationSubmissionRequest request =
        new ConsultationSubmissionRequest(consultationId, "test@example.com", new ArrayList<>());
    Consultation consultation =
        new Consultation(consultationId, "ref-123", "Test Consultation", Collections.emptyList());
    ConsultationSubmissionResponse successResponse =
        new ConsultationSubmissionResponse(true, "Submission successful.");

    when(consultationRepository.getConsultationById(consultationId))
        .thenReturn(Optional.of(consultation));
    when(eligibilityService.evaluateEligibility(consultation, request.answers()))
        .thenReturn(successResponse);

    ConsultationSubmissionResponse response = consultationService.submitConsultation(request);

    // Assert
    assertNotNull(response);
    assertTrue(response.likelyToPrescribe());
    assertEquals("Submission successful.", response.message());
    verify(consultationRepository).getConsultationById(consultationId);
    verify(consultationRepository).saveConsultationSubmission(request);
  }

  @Test
  void testSubmitConsultation_NotFound() {
    UUID consultationId = UUID.randomUUID();
    ConsultationSubmissionRequest request =
        new ConsultationSubmissionRequest(
            consultationId, "test@example.com", Collections.emptyList());

    when(consultationRepository.getConsultationById(consultationId)).thenReturn(Optional.empty());

    ResponseStatusException exception =
        assertThrows(
            ResponseStatusException.class, () -> consultationService.submitConsultation(request));
    assertEquals("404 NOT_FOUND \"Consultation not found\"", exception.getMessage());
    verify(consultationRepository).getConsultationById(consultationId);
    verify(consultationRepository, never()).saveConsultationSubmission(request);
  }
}
