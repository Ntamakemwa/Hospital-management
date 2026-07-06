package spring.hospitalmanagement.DTO;

import lombok.Data;
import java.util.Map;

@Data
public class ReportResponseDTO {
    private long totalPatients;
    private long totalDoctors;
    private long totalAppointments;
    private long cancelledAppointments;
    private long dailyAppointments;
    private long weeklyAppointments;
    private long monthlyAppointments;
    private Map<String, Long> appointmentsPerService;
    private String mostRequestedService;
    private Map<String, Long> doctorWorkload;
}
