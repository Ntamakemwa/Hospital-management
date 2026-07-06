package spring.hospitalmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.hospitalmanagement.Model.Appointment;
import spring.hospitalmanagement.Enums.AppointmentStatus;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByAppointmentDate(LocalDate date);
    int countByDoctorIdAndAppointmentDate(Long doctorId, LocalDate date);
    boolean existsByPatientIdAndAppointmentDateAndAppointmentTime(Long patientId, LocalDate date, java.time.LocalTime time);
    List<Appointment> findByDoctorIdAndStatusIn(Long doctorId, List<AppointmentStatus> statuses);
    List<Appointment> findByAppointmentDateBetween(LocalDate start, LocalDate end);
}
