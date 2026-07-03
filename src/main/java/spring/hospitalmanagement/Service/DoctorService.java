package spring.hospitalmanagement.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.hospitalmanagement.DTO.UserRequestDTO;
import spring.hospitalmanagement.DTO.UserResponseDTO;
import spring.hospitalmanagement.Enums.AppointmentStatus;
import spring.hospitalmanagement.Enums.DoctorStatus;
import spring.hospitalmanagement.Enums.Role;
import spring.hospitalmanagement.Exception.BadRequestException;
import spring.hospitalmanagement.Exception.DuplicateResourceException;
import spring.hospitalmanagement.Exception.ResourceNotFoundException;
import spring.hospitalmanagement.Mapper.DoctorMapper;
import spring.hospitalmanagement.Model.Doctor;
import spring.hospitalmanagement.Model.Hospital;
import spring.hospitalmanagement.Model.HospitalService;
import spring.hospitalmanagement.Model.User;
import spring.hospitalmanagement.Repository.AppointmentRepository;
import spring.hospitalmanagement.Repository.DoctorRepository;
import spring.hospitalmanagement.Repository.HospitalRepository;
import spring.hospitalmanagement.Repository.HospitalServiceRepository;
import spring.hospitalmanagement.Repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;
    private final HospitalServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorMapper doctorMapper;

    @Transactional
    public UserResponseDTO createDoctor(UserRequestDTO request, Long hospitalId) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        if (doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new DuplicateResourceException("License number already exists");
        }

        if (request.getSpecialisation() == null || request.getSpecialisation().isBlank()) {
            throw new BadRequestException("Doctor must have a specialisation");
        }

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found"));

        // Fetch and validate services
        List<HospitalService> services = List.of();
        if (request.getServiceIds() != null && !request.getServiceIds().isEmpty()) {
            services = serviceRepository.findAllById(request.getServiceIds());
            if (services.size() != request.getServiceIds().size()) {
                throw new ResourceNotFoundException("One or more services not found");
            }
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(Role.DOCTOR);
        userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setHospital(hospital);
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setSpecialisation(request.getSpecialisation());
        doctor.setGender(request.getGender());
        doctor.setStatus(DoctorStatus.ACTIVE);
        doctor.setServices(services);
        doctorRepository.save(doctor);

        return doctorMapper.toResponse(doctor);
    }

    @Transactional
    public UserResponseDTO updateDoctor(Long doctorId, UserRequestDTO request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        User user = doctor.getUser();
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);

        if (request.getSpecialisation() != null) {
            doctor.setSpecialisation(request.getSpecialisation());
        }

        // Update services if provided
        if (request.getServiceIds() != null && !request.getServiceIds().isEmpty()) {
            List<HospitalService> services = serviceRepository.findAllById(request.getServiceIds());
            if (services.size() != request.getServiceIds().size()) {
                throw new ResourceNotFoundException("One or more services not found");
            }
            doctor.setServices(services);
        }

        doctorRepository.save(doctor);
        return doctorMapper.toResponse(doctor);
    }

    @Transactional
    public void toggleDoctorStatus(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        if (doctor.getStatus() == DoctorStatus.ACTIVE) {
            doctor.setStatus(DoctorStatus.INACTIVE);
        } else {
            doctor.setStatus(DoctorStatus.ACTIVE);
        }
        doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        boolean hasFutureAppointments = appointmentRepository
                .findByDoctorIdAndStatusIn(doctorId,
                        List.of(AppointmentStatus.PENDING, AppointmentStatus.APPROVED))
                .stream()
                .anyMatch(a -> a.getAppointmentDate().isAfter(LocalDate.now()));

        if (hasFutureAppointments) {
            throw new BadRequestException("Cannot delete doctor with future appointments. Mark as inactive instead.");
        }

        doctorRepository.delete(doctor);
        userRepository.delete(doctor.getUser());
    }

    public List<UserResponseDTO> getDoctorsByHospital(Long hospitalId) {
        return doctorRepository.findByHospitalId(hospitalId)
                .stream()
                .map(doctorMapper::toResponse)
                .toList();
    }
}