package spring.hospitalmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.hospitalmanagement.Model.HospitalService;
import java.util.List;

public interface HospitalServiceRepository extends JpaRepository<HospitalService, Long> {
    boolean existsByNameAndHospitalId(String name, Long hospitalId);
    List<HospitalService> findByHospitalId(Long hospitalId);
}
