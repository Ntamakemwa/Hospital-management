package spring.hospitalmanagement.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.hospitalmanagement.DTO.ServiceRequestDTO;
import spring.hospitalmanagement.DTO.ServiceResponseDTO;
import spring.hospitalmanagement.Service.MedicalService;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class MedicalServiceController {

    private final MedicalService medicalService;

    @PostMapping("/hospital/{hospitalId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ServiceResponseDTO> createService(@Valid @RequestBody ServiceRequestDTO request,
                                                            @PathVariable Long hospitalId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalService.createService(request, hospitalId));
    }

    @PutMapping("/{serviceId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ServiceResponseDTO> updateService(@PathVariable Long serviceId,
                                                            @Valid @RequestBody ServiceRequestDTO request) {
        return ResponseEntity.ok(medicalService.updateService(serviceId, request));
    }

    @DeleteMapping("/{serviceId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteService(@PathVariable Long serviceId) {
        medicalService.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hospital/{hospitalId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_PATIENT')")
    public ResponseEntity<List<ServiceResponseDTO>> getServices(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(medicalService.getServicesByHospital(hospitalId));
    }
}