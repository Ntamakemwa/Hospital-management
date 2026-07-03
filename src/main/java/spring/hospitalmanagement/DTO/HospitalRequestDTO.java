package spring.hospitalmanagement.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HospitalRequestDTO {

    // Hospital info
    @NotBlank(message = "Hospital name is required")
    private String hospitalName;

    @NotBlank(message = "Telephone is required")
    private String telephone;

    @NotBlank(message = "Address is required")
    private String address;

    // Admin info
    @NotBlank(message = "Admin full name is required")
    private String adminFullName;

    @NotBlank(message = "Admin email is required")
    private String adminEmail;

    @NotBlank(message = "Admin password is required")
    private String adminPassword;

    @NotBlank(message = "Admin phone number is required")
    private String adminPhoneNumber;
}
