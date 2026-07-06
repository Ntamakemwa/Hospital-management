package spring.hospitalmanagement.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.hospitalmanagement.DTO.AppointmentRequestDTO;
import spring.hospitalmanagement.DTO.AppointmentResponseDTO;
import spring.hospitalmanagement.Enums.AppointmentStatus;
import spring.hospitalmanagement.Exception.BadRequestException;
import spring.hospitalmanagement.Exception.ResourceNotFoundException;
import spring.hospitalmanagement.Mapper.AppointmentMapper;
import spring.hospitalmanagement.Model.Appointment;
import spring.hospitalmanagement.Model.Doctor;
import spring.hospitalmanagement.Model.HospitalService;
import spring.hospitalmanagement.Model.Patient;
import spring.hospitalmanagement.Repository.AppointmentRepository;
import spring.hospitalmanagement.Repository.DoctorRepository;
import spring.hospitalmanagement.Repository.HospitalServiceRepository;
import spring.hospitalmanagement.Repository.PatientRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final HospitalServiceRepository serviceRepository;
    private final AppointmentMapper appointmentMapper;

    @Transactional
    public AppointmentResponseDTO bookAppointment(AppointmentRequestDTO request, Long userId) {

        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        HospitalService service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        int count = appointmentRepository.countByDoctorIdAndAppointmentDate(
                doctor.getId(), request.getAppointmentDate());
        if (count >= 4) {
            throw new BadRequestException("Doctor already has 4 appointments on this day");
        }

        boolean clash = appointmentRepository.existsByPatientIdAndAppointmentDateAndAppointmentTime(
                patient.getId(), request.getAppointmentDate(), request.getAppointmentTime());
        if (clash) {
            throw new BadRequestException("You already have an appointment at this date and time");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setService(service);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setSymptoms(request.getSymptoms());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointmentRepository.save(appointment);

        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    public AppointmentResponseDTO approveAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(AppointmentStatus.APPROVED);
        appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    public AppointmentResponseDTO rejectAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(AppointmentStatus.REJECTED);
        appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    public AppointmentResponseDTO cancelAppointment(Long appointmentId, Long userId) {
        Appointment appointment = getAppointmentById(appointmentId);

        if (appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot cancel a past appointment");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    public AppointmentResponseDTO completeAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    public AppointmentResponseDTO reassignAppointment(Long appointmentId, Long newDoctorId) {
        Appointment appointment = getAppointmentById(appointmentId);

        Doctor newDoctor = doctorRepository.findById(newDoctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        appointment.setDoctor(newDoctor);
        appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(appointment);
    }

    public List<AppointmentResponseDTO> getPatientAppointments(Long userId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return appointmentRepository.findByPatientId(patient.getId())
                .stream().map(appointmentMapper::toResponse).toList();
    }

    public List<AppointmentResponseDTO> getDoctorAppointments(Long userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return appointmentRepository.findByDoctorId(doctor.getId())
                .stream().map(appointmentMapper::toResponse).toList();
    }

    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream().map(appointmentMapper::toResponse).toList();
    }

    private Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    }
}