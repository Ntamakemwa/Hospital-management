package spring.hospitalmanagement.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.hospitalmanagement.DTO.HospitalRequestDTO;
import spring.hospitalmanagement.DTO.HospitalResponseDTO;
import spring.hospitalmanagement.Enums.Role;
import spring.hospitalmanagement.Exception.DuplicateResourceException;
import spring.hospitalmanagement.Exception.ResourceNotFoundException;
import spring.hospitalmanagement.Mapper.HospitalMapper;
import spring.hospitalmanagement.Model.Hospital;
import spring.hospitalmanagement.Model.User;
import spring.hospitalmanagement.Repository.HospitalRepository;
import spring.hospitalmanagement.Repository.UserRepository;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HospitalMapper hospitalMapper;

    @Transactional
    public HospitalResponseDTO registerHospital(HospitalRequestDTO request) {

        if (hospitalRepository.existsByName(request.getHospitalName())) {
            throw new DuplicateResourceException("Hospital name already exists");
        }

        if (userRepository.existsByEmail(request.getAdminEmail())) {
            throw new DuplicateResourceException("Admin email already exists");
        }

        User admin = new User();
        admin.setFullName(request.getAdminFullName());
        admin.setEmail(request.getAdminEmail());
        admin.setPassword(passwordEncoder.encode(request.getAdminPassword()));
        admin.setPhoneNumber(request.getAdminPhoneNumber());
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        Hospital hospital = new Hospital();
        hospital.setName(request.getHospitalName());
        hospital.setTelephone(request.getTelephone());
        hospital.setAddress(request.getAddress());
        hospital.setAdmin(admin);
        hospitalRepository.save(hospital);

        return hospitalMapper.toResponse(hospital);
    }
}