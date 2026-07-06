package spring.hospitalmanagement.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DiagnosisRequestDTO {

    @NotNull(message = "Appointment is required")
    private Long appointmentId;

    @NotBlank(message = "Symptoms are required")
    private String symptoms;

    @NotBlank(message = "Diagnosis notes are required")
    private String diagnosisNotes;
}
