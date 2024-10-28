package com.foulsham.demo.heliosx.mapper;

import com.foulsham.demo.heliosx.dto.consultation.BooleanQuestion;
import com.foulsham.demo.heliosx.dto.consultation.ConsultationDto;
import com.foulsham.demo.heliosx.model.consultation.Consultation;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ConsultationMapper {
  public ConsultationDto toDto(Consultation consultation) {
    return new ConsultationDto(
        consultation.id(),
        consultation.reference(),
        consultation.name(),
        consultation.questions().stream()
            .map(question -> new BooleanQuestion(question.id(), question.question()))
            .collect(Collectors.toList()));
  }
}
