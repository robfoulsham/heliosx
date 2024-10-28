package com.foulsham.demo.heliosx.dto.consultation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record ConsultationSubmissionRequest(
    @NotNull(message = "Id is required") UUID id,
    @NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email,
    List<BooleanAnswer> answers) {}
