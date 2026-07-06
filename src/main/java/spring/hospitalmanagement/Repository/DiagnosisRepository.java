package spring.hospitalmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.hospitalmanagement.Model.Diagnosis;
import java.util.Optional;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    Optional<Diagnosis> findByAppointmentId(Long appointmentId);
}
