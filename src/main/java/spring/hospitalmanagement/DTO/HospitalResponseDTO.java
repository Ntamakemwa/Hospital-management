package spring.hospitalmanagement.DTO;

import lombok.Data;

@Data
public class HospitalResponseDTO {
    private Long id;
    private String hospitalName;
    private String telephone;
    private String address;
    private String adminFullName;
    private String adminEmail;
}
