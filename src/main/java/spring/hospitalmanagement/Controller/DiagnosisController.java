package spring.hospitalmanagement.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.hospitalmanagement.DTO.DiagnosisRequestDTO;
import spring.hospitalmanagement.DTO.DiagnosisResponseDTO;
import spring.hospitalmanagement.Model.User;
import spring.hospitalmanagement.Repository.UserRepository;
import spring.hospitalmanagement.Service.DiagnosisService;

import java.io.IOException;

@RestController
@RequestMapping("/api/diagnoses")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;
    private final UserRepository userRepository;

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DiagnosisResponseDTO> createDiagnosis(
            @Valid @RequestPart("data") DiagnosisRequestDTO request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        Long userId = getUserId(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(diagnosisService.createDiagnosis(request, file, userId));
    }

    @PutMapping("/{diagnosisId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DiagnosisResponseDTO> updateDiagnosis(
            @PathVariable Long diagnosisId,
            @Valid @RequestBody DiagnosisRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(diagnosisService.updateDiagnosis(diagnosisId, request, userId));
    }

    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<DiagnosisResponseDTO> getDiagnosis(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(diagnosisService.getDiagnosis(appointmentId));
    }

    private Long getUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}
