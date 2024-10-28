package com.foulsham.demo.heliosx.model.consultation;

import java.util.UUID;

public record BooleanQuestion(UUID id, String question, Boolean requiredAnswer) {}
