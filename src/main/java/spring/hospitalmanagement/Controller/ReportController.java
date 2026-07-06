package spring.hospitalmanagement.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.hospitalmanagement.DTO.ReportResponseDTO;
import spring.hospitalmanagement.Service.ReportService;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ReportResponseDTO> getReport() {
        return ResponseEntity.ok(reportService.generateReport());
    }
}
