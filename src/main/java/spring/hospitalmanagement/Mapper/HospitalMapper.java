package spring.hospitalmanagement.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.hospitalmanagement.DTO.HospitalResponseDTO;
import spring.hospitalmanagement.Model.Hospital;

@Mapper(componentModel = "spring")
public interface HospitalMapper {

    @Mapping(source = "name", target = "hospitalName")
    @Mapping(source = "admin.fullName", target = "adminFullName")
    @Mapping(source = "admin.email", target = "adminEmail")
    HospitalResponseDTO toResponse(Hospital hospital);
}
