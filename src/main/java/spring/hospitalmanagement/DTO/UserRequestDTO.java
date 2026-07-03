package spring.hospitalmanagement.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import spring.hospitalmanagement.Enums.Gender;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserRequestDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String nationalId;

    @Past(message = "Date of birth cannot be in the future")
    private LocalDate dateOfBirth;

    private Gender gender;

    private String address;

    private String emergencyContactName;

    private String emergencyContactPhone;

    private String licenseNumber;

    private String specialisation;

    // Doctor services
    private List<Long> serviceIds;
}