package spring.hospitalmanagement.DTO;

import lombok.Data;
import spring.hospitalmanagement.Enums.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentResponseDTO {
    private Long id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String symptoms;
    private AppointmentStatus status;
    private String patientName;
    private String doctorName;
    private String serviceName;
}
