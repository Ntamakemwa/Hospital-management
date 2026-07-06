package spring.hospitalmanagement.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ServiceRequestDTO {

    @NotBlank(message = "Service name is required")
    private String name;
}
