package spring.hospitalmanagement.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.hospitalmanagement.DTO.UserRequestDTO;
import spring.hospitalmanagement.DTO.UserResponseDTO;
import spring.hospitalmanagement.Service.DoctorService;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping("/hospital/{hospitalId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDTO> createDoctor(@Valid @RequestBody UserRequestDTO request,
                                                        @PathVariable Long hospitalId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(request, hospitalId));
    }

    @PutMapping("/{doctorId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDTO> updateDoctor(@PathVariable Long doctorId,
                                                        @Valid @RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(doctorService.updateDoctor(doctorId, request));
    }

    @PatchMapping("/{doctorId}/toggle-status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> toggleStatus(@PathVariable Long doctorId) {
        doctorService.toggleDoctorStatus(doctorId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{doctorId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hospital/{hospitalId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getDoctorsByHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(doctorService.getDoctorsByHospital(hospitalId));
    }
}