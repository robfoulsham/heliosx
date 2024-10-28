package com.foulsham.demo.heliosx.mapper;

import com.foulsham.demo.heliosx.dto.consultation.ConsultationDto;
import com.foulsham.demo.heliosx.model.consultation.BooleanQuestion;
import com.foulsham.demo.heliosx.model.consultation.Consultation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsultationMapperTest {

  private ConsultationMapper consultationMapper;

  @BeforeEach
  void setUp() {
    consultationMapper = new ConsultationMapper();
  }

  @Test
  void testToDto() {
    UUID consultationId = UUID.randomUUID();
    String reference = "REF123";
    String name = "Test Consultation";
    BooleanQuestion question1 = new BooleanQuestion(UUID.randomUUID(), "Do you have allergies?", true);
    BooleanQuestion question2 = new BooleanQuestion(UUID.randomUUID(), "Have you taken medication before?", true);
    Consultation consultation = new Consultation(consultationId, reference, name, Arrays.asList(question1, question2));

    ConsultationDto result = consultationMapper.toDto(consultation);

    assertEquals(consultationId, result.id());
    assertEquals(reference, result.reference());
    assertEquals(name, result.name());
    assertEquals(2, result.questions().size());
    assertEquals(question1.id(), result.questions().get(0).id());
    assertEquals(question1.question(), result.questions().get(0).question());
    assertEquals(question2.id(), result.questions().get(1).id());
    assertEquals(question2.question(), result.questions().get(1).question());
  }
}
