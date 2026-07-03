package spring.hospitalmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.hospitalmanagement.Model.Doctor;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserId(Long userId);
    boolean existsByLicenseNumber(String licenseNumber);
    List<Doctor> findByHospitalId(Long hospitalId);
}
