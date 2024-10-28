package com.foulsham.demo.heliosx.model.consultation;

import java.util.List;
import java.util.UUID;

public record Consultation(
    UUID id, String reference, String name, List<BooleanQuestion> questions) {}
