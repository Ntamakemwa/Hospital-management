package spring.hospitalmanagement.DTO;

import lombok.Data;
import spring.hospitalmanagement.Enums.DoctorStatus;
import spring.hospitalmanagement.Enums.Gender;
import spring.hospitalmanagement.Enums.Role;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Role role;

    // Patient fields
    private String nationalId;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhone;

    // Doctor fields
    private String licenseNumber;
    private String specialisation;
    private DoctorStatus status;
    private List<String> services;
}