package spring.hospitalmanagement.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import spring.hospitalmanagement.DTO.AppointmentRequestDTO;
import spring.hospitalmanagement.DTO.AppointmentResponseDTO;
import spring.hospitalmanagement.Model.User;
import spring.hospitalmanagement.Repository.UserRepository;
import spring.hospitalmanagement.Service.AppointmentService;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<AppointmentResponseDTO> bookAppointment(
            @Valid @RequestBody AppointmentRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.bookAppointment(request, userId));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppointmentResponseDTO> approve(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.approveAppointment(id));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppointmentResponseDTO> reject(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.rejectAppointment(id));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<AppointmentResponseDTO> cancel(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(appointmentService.cancelAppointment(id, userId));
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppointmentResponseDTO> complete(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.completeAppointment(id));
    }

    @PatchMapping("/{id}/reassign/{doctorId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppointmentResponseDTO> reassign(@PathVariable Long id,
                                                           @PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.reassignAppointment(id, doctorId));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<List<AppointmentResponseDTO>> myAppointments(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(appointmentService.getPatientAppointments(userId));
    }

    @GetMapping("/doctor")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<List<AppointmentResponseDTO>> doctorAppointments(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(userId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AppointmentResponseDTO>> allAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    private Long getUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}