package com.foulsham.demo.heliosx.controller;

import com.foulsham.demo.heliosx.dto.consultation.ConsultationDto;
import com.foulsham.demo.heliosx.dto.consultation.ConsultationSubmissionRequest;
import com.foulsham.demo.heliosx.dto.consultation.ConsultationSubmissionResponse;
import com.foulsham.demo.heliosx.service.ConsultationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/consultation")
public class ConsultationController {

  private final ConsultationService consultationService;

  public ConsultationController(ConsultationService consultationService) {
    this.consultationService = consultationService;
  }

  @Operation(
      summary = "Get a Consultation by Reference",
      description =
          "Retrieve the details of a specific consultation using the consultation reference.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Consultation found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ConsultationDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Consultation not found",
            content = @Content)
      })
  @GetMapping(value = "/{reference}", produces = "application/json")
  public ResponseEntity<ConsultationDto> getConsultationByReference(
      @PathVariable String reference) {
    ConsultationDto consultation = consultationService.getConsultationByReference(reference);
    return ResponseEntity.ok(consultation);
  }

  @Operation(
      summary = "Submit a Consultation",
      description = "Submit a consultation response for a specific prescription.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Consultation submitted",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ConsultationSubmissionResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Consultation not found",
            content = @Content),
        @ApiResponse(
            responseCode = "409",
            description = "Consultation already submitted / duplicate submission",
            content = @Content)
      })
  @PostMapping(produces = "application/json")
  public ResponseEntity<ConsultationSubmissionResponse> submitConsultation(
      @RequestBody @Valid ConsultationSubmissionRequest request) {
    ConsultationSubmissionResponse response = consultationService.submitConsultation(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
