package spring.hospitalmanagement.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.hospitalmanagement.DTO.DiagnosisResponseDTO;
import spring.hospitalmanagement.Model.Diagnosis;

@Mapper(componentModel = "spring")
public interface DiagnosisMapper {

    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(source = "appointment.patient.user.fullName", target = "patientName")
    @Mapping(source = "appointment.doctor.user.fullName", target = "doctorName")
    DiagnosisResponseDTO toResponse(Diagnosis diagnosis);
}
