package com.example.authorization.mailhog;

import com.example.authorization.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;
    private final UserRepository userRepository;

    @PostMapping("/send")
    public ResponseEntity<?> sendMail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text,
            HttpServletRequest request,
            Principal principal
    ) {

        String username = principal.getName();
        String ip = request.getRemoteAddr();
        String agent = request.getHeader("User-Agent");

        emailService.sendEmail(to, subject, text);

        // log it
        System.out.println("EMAIL SENT BY: " + username);
        System.out.println("IP: " + ip);
        System.out.println("AGENT: " + agent);

        return ResponseEntity.ok("Email sent (check MailHog)");
    }
}