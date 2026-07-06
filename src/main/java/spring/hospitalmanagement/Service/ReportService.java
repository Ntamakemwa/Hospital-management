package spring.hospitalmanagement.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.hospitalmanagement.DTO.ReportResponseDTO;
import spring.hospitalmanagement.Enums.AppointmentStatus;
import spring.hospitalmanagement.Repository.DoctorRepository;
import spring.hospitalmanagement.Repository.PatientRepository;
import spring.hospitalmanagement.Repository.ReportRepository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public ReportResponseDTO generateReport() {
        ReportResponseDTO report = new ReportResponseDTO();

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(7);
        LocalDate monthStart = today.minusDays(30);

        // Counts
        report.setTotalPatients(patientRepository.count());
        report.setTotalDoctors(doctorRepository.count());
        report.setTotalAppointments(reportRepository.count());
        report.setCancelledAppointments(
                reportRepository.countByStatus(AppointmentStatus.CANCELLED));

        // Daily
        report.setDailyAppointments(
                reportRepository.findByAppointmentDate(today).size());

        // Weekly
        report.setWeeklyAppointments(
                reportRepository.findByAppointmentDateBetween(weekStart, today).size());

        // Monthly
        report.setMonthlyAppointments(
                reportRepository.findByAppointmentDateBetween(monthStart, today).size());

        // Appointments per service
        List<Object[]> perService = reportRepository.countAppointmentsPerService();
        Map<String, Long> serviceMap = new LinkedHashMap<>();
        String mostRequested = null;
        long maxCount = 0;
        for (Object[] row : perService) {
            String serviceName = (String) row[0];
            Long count = (Long) row[1];
            serviceMap.put(serviceName, count);
            if (count > maxCount) {
                maxCount = count;
                mostRequested = serviceName;
            }
        }
        report.setAppointmentsPerService(serviceMap);
        report.setMostRequestedService(mostRequested);

        // Doctor workload
        List<Object[]> perDoctor = reportRepository.countAppointmentsPerDoctor();
        Map<String, Long> doctorMap = new LinkedHashMap<>();
        for (Object[] row : perDoctor) {
            doctorMap.put((String) row[0], (Long) row[1]);
        }
        report.setDoctorWorkload(doctorMap);

        return report;
    }
}
