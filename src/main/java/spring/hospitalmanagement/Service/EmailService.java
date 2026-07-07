package spring.hospitalmanagement.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendWelcomeEmail(String to, String fullName) {
        sendEmail(to,
                "Welcome to Hospital Management System",
                "Dear " + fullName + ",\n\nWelcome! Your account has been created successfully.\n\nRegards,\nHospital Management System");
    }

    public void sendAppointmentBookedEmail(String to, String fullName, String date, String time) {
        sendEmail(to,
                "Appointment Booked",
                "Dear " + fullName + ",\n\nYour appointment has been booked for " + date + " at " + time + ".\n\nRegards,\nHospital Management System");
    }

    public void sendAppointmentApprovedEmail(String to, String fullName, String date, String time) {
        sendEmail(to,
                "Appointment Approved",
                "Dear " + fullName + ",\n\nYour appointment on " + date + " at " + time + " has been approved.\n\nRegards,\nHospital Management System");
    }

    public void sendAppointmentRejectedEmail(String to, String fullName) {
        sendEmail(to,
                "Appointment Rejected",
                "Dear " + fullName + ",\n\nUnfortunately your appointment has been rejected. Please book another date.\n\nRegards,\nHospital Management System");
    }

    public void sendAppointmentCancelledEmail(String to, String fullName) {
        sendEmail(to,
                "Appointment Cancelled",
                "Dear " + fullName + ",\n\nYour appointment has been cancelled.\n\nRegards,\nHospital Management System");
    }

    public void sendDoctorCreatedEmail(String to, String fullName, String password) {
        sendEmail(to,
                "Your Doctor Account",
                "Dear Dr. " + fullName + ",\n\nYour account has been created.\nEmail: " + to + "\nPassword: " + password + "\n\nPlease login and change your password.\n\nRegards,\nHospital Management System");
    }
}
