package spring.hospitalmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.hospitalmanagement.Enums.AppointmentStatus;
import spring.hospitalmanagement.Model.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends JpaRepository<Appointment, Long> {

    // Daily
    List<Appointment> findByAppointmentDate(LocalDate date);

    // Weekly and Monthly
    List<Appointment> findByAppointmentDateBetween(LocalDate start, LocalDate end);

    // Count by status
    long countByStatus(AppointmentStatus status);

    // Appointments per service
    @Query("SELECT a.service.name, COUNT(a) FROM Appointment a GROUP BY a.service.name")
    List<Object[]> countAppointmentsPerService();

    // Doctor workload
    @Query("SELECT a.doctor.user.fullName, COUNT(a) FROM Appointment a GROUP BY a.doctor.user.fullName")
    List<Object[]> countAppointmentsPerDoctor();
}
