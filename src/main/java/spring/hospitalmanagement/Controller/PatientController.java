package spring.hospitalmanagement.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import spring.hospitalmanagement.DTO.UserRequestDTO;
import spring.hospitalmanagement.DTO.UserResponseDTO;
import spring.hospitalmanagement.Model.User;
import spring.hospitalmanagement.Repository.UserRepository;
import spring.hospitalmanagement.Service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.register(request));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<UserResponseDTO> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(patientService.getProfile(userId));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<UserResponseDTO> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                                         @Valid @RequestBody UserRequestDTO request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(patientService.updateProfile(userId, request));
    }

    @PatchMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<UserResponseDTO> patchProfile(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestBody UserRequestDTO request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(patientService.patchProfile(userId, request));
    }

    @DeleteMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        patientService.deleteAccount(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    private Long getUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}