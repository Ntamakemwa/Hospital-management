package spring.hospitalmanagement.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.hospitalmanagement.DTO.ServiceResponseDTO;
import spring.hospitalmanagement.Model.HospitalService;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    @Mapping(source = "hospital.name", target = "hospitalName")
    ServiceResponseDTO toResponse(HospitalService hospitalService);
}
