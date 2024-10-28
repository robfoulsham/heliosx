package com.foulsham.demo.heliosx.dto.consultation;

import java.util.List;
import java.util.UUID;

public record ConsultationDto(
    UUID id, String reference, String name, List<BooleanQuestion> questions) {}
