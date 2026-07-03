package spring.hospitalmanagement.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.hospitalmanagement.DTO.HospitalRequestDTO;
import spring.hospitalmanagement.DTO.HospitalResponseDTO;
import spring.hospitalmanagement.Service.HospitalService;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @PostMapping("/register")
    public ResponseEntity<HospitalResponseDTO> register(@Valid @RequestBody HospitalRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hospitalService.registerHospital(request));
    }
}
