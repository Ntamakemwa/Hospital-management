package spring.hospitalmanagement.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.hospitalmanagement.DTO.AppointmentResponseDTO;
import spring.hospitalmanagement.Model.Appointment;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "patient.user.fullName", target = "patientName")
    @Mapping(source = "doctor.user.fullName", target = "doctorName")
    @Mapping(source = "service.name", target = "serviceName")
    AppointmentResponseDTO toResponse(Appointment appointment);
}
