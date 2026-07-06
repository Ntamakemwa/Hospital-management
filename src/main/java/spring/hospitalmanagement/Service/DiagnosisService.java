package spring.hospitalmanagement.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.hospitalmanagement.DTO.DiagnosisRequestDTO;
import spring.hospitalmanagement.DTO.DiagnosisResponseDTO;
import spring.hospitalmanagement.Enums.AppointmentStatus;
import spring.hospitalmanagement.Exception.BadRequestException;
import spring.hospitalmanagement.Exception.ResourceNotFoundException;
import spring.hospitalmanagement.Exception.UnauthorizedException;
import spring.hospitalmanagement.Mapper.DiagnosisMapper;
import spring.hospitalmanagement.Model.Appointment;
import spring.hospitalmanagement.Model.Diagnosis;
import spring.hospitalmanagement.Model.Doctor;
import spring.hospitalmanagement.Repository.AppointmentRepository;
import spring.hospitalmanagement.Repository.DiagnosisRepository;
import spring.hospitalmanagement.Repository.DoctorRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final DiagnosisMapper diagnosisMapper;

    private static final String UPLOAD_DIR = "uploads/prescriptions/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    @Transactional
    public DiagnosisResponseDTO createDiagnosis(DiagnosisRequestDTO request,
                                                MultipartFile file,
                                                Long userId) throws IOException {

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot diagnose a completed appointment");
        }

        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new UnauthorizedException("You are not assigned to this appointment");
        }

        String filePath = null;
        if (file != null && !file.isEmpty()) {
            validateFile(file);
            filePath = saveFile(file);
        }

        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setAppointment(appointment);
        diagnosis.setSymptoms(request.getSymptoms());
        diagnosis.setDiagnosisNotes(request.getDiagnosisNotes());
        diagnosis.setPrescriptionFilePath(filePath);
        diagnosisRepository.save(diagnosis);

        return diagnosisMapper.toResponse(diagnosis);
    }

    @Transactional
    public DiagnosisResponseDTO updateDiagnosis(Long diagnosisId,
                                                DiagnosisRequestDTO request,
                                                Long userId) {

        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found"));

        if (diagnosis.getAppointment().getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot update diagnosis of a completed appointment");
        }

        diagnosis.setSymptoms(request.getSymptoms());
        diagnosis.setDiagnosisNotes(request.getDiagnosisNotes());
        diagnosisRepository.save(diagnosis);

        return diagnosisMapper.toResponse(diagnosis);
    }

    public DiagnosisResponseDTO getDiagnosis(Long appointmentId) {
        Diagnosis diagnosis = diagnosisRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found"));
        return diagnosisMapper.toResponse(diagnosis);
    }

    private void validateFile(MultipartFile file) {
        if (!file.getContentType().equals("application/pdf")) {
            throw new BadRequestException("Only PDF files are allowed");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds 5MB limit");
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return filePath.toString();
    }
}