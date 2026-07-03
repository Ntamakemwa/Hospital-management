package spring.hospitalmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.hospitalmanagement.Model.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    boolean existsByName(String name);
}
