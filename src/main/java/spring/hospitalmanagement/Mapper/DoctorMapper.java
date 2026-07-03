package spring.hospitalmanagement.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import spring.hospitalmanagement.DTO.UserResponseDTO;
import spring.hospitalmanagement.Model.Doctor;
import spring.hospitalmanagement.Model.HospitalService;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.phoneNumber", target = "phoneNumber")
    @Mapping(source = "user.role", target = "role")
    @Mapping(source = "services", target = "services", qualifiedByName = "mapServices")
    UserResponseDTO toResponse(Doctor doctor);

    @Named("mapServices")
    default List<String> mapServices(List<HospitalService> services) {
        if (services == null) return List.of();
        return services.stream()
                .map(HospitalService::getName)
                .collect(Collectors.toList());
    }
}