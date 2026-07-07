package spring.hospitalmanagement.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.hospitalmanagement.Enums.Gender;
import spring.hospitalmanagement.Enums.Role;
import spring.hospitalmanagement.Model.Patient;
import spring.hospitalmanagement.Model.User;
import spring.hospitalmanagement.Repository.PatientRepository;
import spring.hospitalmanagement.Repository.UserRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final EmailService emailService;

    @Transactional
    public User processOAuthUser(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("name");

        return userRepository.findByEmail(email).orElseGet(() -> {
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPassword("OAUTH2_USER");
            user.setPhoneNumber("N/A");
            user.setRole(Role.PATIENT);
            userRepository.save(user);

            Patient patient = new Patient();
            patient.setUser(user);
            patient.setGender(Gender.MALE);
            patient.setDateOfBirth(LocalDate.of(2000, 1, 1));
            patient.setAddress("N/A");
            patient.setEmergencyContactName("N/A");
            patient.setEmergencyContactPhone("N/A");
            patientRepository.save(patient);


            try {
                emailService.sendWelcomeEmail(email, fullName);
            } catch (Exception e) {
                System.out.println("Email failed: " + e.getMessage());
            }

            return user;
        });
    }
}