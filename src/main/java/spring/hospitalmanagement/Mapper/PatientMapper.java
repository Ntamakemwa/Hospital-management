package spring.hospitalmanagement.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.hospitalmanagement.DTO.UserResponseDTO;
import spring.hospitalmanagement.Model.Patient;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.phoneNumber", target = "phoneNumber")
    @Mapping(source = "user.role", target = "role")
    UserResponseDTO toResponse(Patient patient);
}
