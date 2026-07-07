package spring.hospitalmanagement.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.hospitalmanagement.DTO.UserRequestDTO;
import spring.hospitalmanagement.DTO.UserResponseDTO;
import spring.hospitalmanagement.Enums.AppointmentStatus;
import spring.hospitalmanagement.Enums.Role;
import spring.hospitalmanagement.Exception.BadRequestException;
import spring.hospitalmanagement.Exception.DuplicateResourceException;
import spring.hospitalmanagement.Exception.ResourceNotFoundException;
import spring.hospitalmanagement.Mapper.PatientMapper;
import spring.hospitalmanagement.Model.Patient;
import spring.hospitalmanagement.Model.User;
import spring.hospitalmanagement.Repository.AppointmentRepository;
import spring.hospitalmanagement.Repository.PatientRepository;
import spring.hospitalmanagement.Repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientMapper patientMapper;
    private final EmailService emailService;

    @Transactional
    public UserResponseDTO register(UserRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        if (request.getNationalId() != null && patientRepository.existsByNationalId(request.getNationalId())) {
            throw new DuplicateResourceException("National ID already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(Role.PATIENT);
        userRepository.save(user);

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setNationalId(request.getNationalId());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        patientRepository.save(patient);
        emailService.sendWelcomeEmail(user.getEmail(), user.getFullName());

        return patientMapper.toResponse(patient);
    }

    public UserResponseDTO getProfile(Long userId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return patientMapper.toResponse(patient);
    }

    @Transactional
    public UserResponseDTO updateProfile(Long userId, UserRequestDTO request) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        User user = patient.getUser();
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);

        patient.setAddress(request.getAddress());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        patientRepository.save(patient);

        return patientMapper.toResponse(patient);
    }

    @Transactional
    public UserResponseDTO patchProfile(Long userId, UserRequestDTO request) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        User user = patient.getUser();
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);

        if (request.getAddress() != null) patient.setAddress(request.getAddress());
        if (request.getEmergencyContactName() != null) patient.setEmergencyContactName(request.getEmergencyContactName());
        if (request.getEmergencyContactPhone() != null) patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        patientRepository.save(patient);

        return patientMapper.toResponse(patient);
    }

    @Transactional
    public void deleteAccount(Long userId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        boolean hasActiveAppointments = appointmentRepository
                .findByPatientId(patient.getId())
                .stream()
                .anyMatch(a -> a.getStatus() == AppointmentStatus.PENDING
                        || a.getStatus() == AppointmentStatus.APPROVED);

        if (hasActiveAppointments) {
            throw new BadRequestException("Cannot delete account with pending or approved appointments");
        }

        patientRepository.delete(patient);
        userRepository.delete(patient.getUser());
    }

    public List<UserResponseDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toResponse)
                .toList();
    }
}