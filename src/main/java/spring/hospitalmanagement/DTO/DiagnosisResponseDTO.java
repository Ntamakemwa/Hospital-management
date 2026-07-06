package spring.hospitalmanagement.DTO;

import lombok.Data;

@Data
public class DiagnosisResponseDTO {
    private Long id;
    private Long appointmentId;
    private String symptoms;
    private String diagnosisNotes;
    private String prescriptionFilePath;
    private String patientName;
    private String doctorName;
}
