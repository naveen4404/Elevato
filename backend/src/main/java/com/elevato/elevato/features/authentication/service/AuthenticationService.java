package com.elevato.elevato.features.authentication.service;

import com.elevato.elevato.exception.BadRequestException;
import com.elevato.elevato.exception.ResourceNotFoundException;
import com.elevato.elevato.features.authentication.dto.AuthenticationUserRequest;
import com.elevato.elevato.features.authentication.dto.AuthenticationUserResponse;
import com.elevato.elevato.features.authentication.model.AuthenticationUser;
import com.elevato.elevato.features.authentication.repository.AuthenticationUserRepository;
import com.elevato.elevato.features.authentication.utils.EmailService;
import com.elevato.elevato.features.authentication.utils.Encoder;
import com.elevato.elevato.features.authentication.utils.JsonWebToken;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private static final int durationInMinutes = 5;
    private final AuthenticationUserRepository authenticationUserRepository;
    private final Encoder encoder;
    private final JsonWebToken jsonWebToken;
    private final EmailService emailService;

    @PersistenceContext
    private EntityManager entityManager;

    public AuthenticationService(AuthenticationUserRepository authenticationUserRepository, Encoder encoder, JsonWebToken jsonWebToken, EmailService emailService) {
        this.authenticationUserRepository = authenticationUserRepository;
        this.encoder = encoder;
        this.jsonWebToken = jsonWebToken;
        this.emailService = emailService;
    }

    public static String generateEmailVerificationToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder();

        for(int i = 1; i<6;i++){
            token.append(random.nextInt(10));
        }
        return token.toString();
    }

    // Email Verification
    public void sendEmailVerificationToken(String email){
        Optional<AuthenticationUser> user = authenticationUserRepository.findByEmail(email);

        if(user.isPresent() && !user.get().getEmailVerified()){
            String emailVerificationToken = generateEmailVerificationToken();
            String hashToken = encoder.encode(emailVerificationToken);
            user.get().setEmailVerificationToken(hashToken);
            user.get().setEmailVerificationTokenExpiryDate(LocalDateTime.now().plusMinutes(durationInMinutes));
            authenticationUserRepository.save(user.get());

            String subject = "Email verification token";

            String body = String.format("""
                Only one step away.
                Enter this code to verify your email: %s
                The code will expire in %s minutes.
                """, emailVerificationToken, durationInMinutes
            );

            try{
                emailService.sendMail(email, subject, body);
            } catch (Exception e) {
               logger.info("Error while sending email: {}", e.getMessage());
            }

        }else{
            throw new BadRequestException("Email verification token failed, or email is already verified");
        }
    }

    public void validateEmailVerificationToken(String token, String email){
        Optional<AuthenticationUser> user = authenticationUserRepository.findByEmail(email);

        if(user.isPresent()
                && encoder.match(token, user.get().getEmailVerificationToken())
                && !user.get().getEmailVerificationTokenExpiryDate().isBefore(LocalDateTime.now())){
            user.get().setEmailVerified(true);
            user.get().setEmailVerificationToken(null);
            user.get().setEmailVerificationTokenExpiryDate(null);
            authenticationUserRepository.save(user.get());
        } else if (user.isPresent()
                && encoder.match(token, user.get().getEmailVerificationToken())
                && user.get().getEmailVerificationTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Email verification token expired");
        }else{
            throw new BadRequestException("Email verification token failed");
        }
    }

    public AuthenticationUser getUser(String email){
        return authenticationUserRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public AuthenticationUserResponse register(AuthenticationUserRequest newUser) throws MessagingException, UnsupportedEncodingException {
        AuthenticationUser user = new AuthenticationUser(newUser.getEmail(), encoder.encode(newUser.getPassword()));
        authenticationUserRepository.save(user);

        String emailVerificationToken = generateEmailVerificationToken();
        String hashedToken = encoder.encode(emailVerificationToken);
        user.setEmailVerificationToken(hashedToken);
        user.setEmailVerificationTokenExpiryDate(LocalDateTime.now().plusMinutes(durationInMinutes));
        authenticationUserRepository.save(user);

        String subject = "Email verification token";

        String body = String.format("""
                Only one step away.
                Enter this code to verify your email: %s
                The code will expire in %s minutes.
                """, emailVerificationToken, durationInMinutes
        );

        try{
            emailService.sendMail(newUser.getEmail(), subject, body);
        } catch (Exception e) {
            logger.info("Error while sending verification email: {}", e.getMessage());
        }

        String token = jsonWebToken.generateToken(user.getEmail());
        return new AuthenticationUserResponse(token,"User registered successfully");
    }

    public AuthenticationUserResponse login( AuthenticationUserRequest loginUser) {
        AuthenticationUser user = getUser(loginUser.getEmail());

        if(!encoder.match(loginUser.getPassword(), user.getPassword())){
            throw new BadRequestException("Incorrect password");
        }

        String token = jsonWebToken.generateToken(loginUser.getEmail());

        return new AuthenticationUserResponse(token, "Logged in successfully");
    }


    // Password reset
    public void sendPasswordResetToken(String email){
        Optional<AuthenticationUser> user = authenticationUserRepository.findByEmail(email);

        if(user.isPresent()){
            String token = generateEmailVerificationToken();
            String hashedToken = encoder.encode(token);
            user.get().setPasswordResetToken(hashedToken);
            user.get().setPasswordResetTokenExpiryDate(LocalDateTime.now().plusMinutes(durationInMinutes));
            authenticationUserRepository.save(user.get());

            String subject = "Password reset";
            String body = String.format("""
                    You requested a password reset.
                    Enter this code to reset your password: %s
                    The code will expire in %s
                    """,
                    token, durationInMinutes
            );

            try{
                emailService.sendMail(email, subject, body);
            } catch (Exception e) {
                logger.info("Error while sending password reset email: {}", e.getMessage());
            }

        }else{
            throw new ResourceNotFoundException("User not found");
        }
    }

    public void resetPassword(String email, String newPassword, String token){
        Optional<AuthenticationUser> user = authenticationUserRepository.findByEmail(email);
        if(user.isPresent()
                && encoder.match(token, user.get().getPasswordResetToken())
                && !user.get().getPasswordResetTokenExpiryDate().isBefore(LocalDateTime.now())){
            user.get().setPasswordResetToken(null);
            user.get().setPasswordResetTokenExpiryDate(null);
            user.get().setPassword(encoder.encode(newPassword));
            authenticationUserRepository.save(user.get());
        }else if(user.isPresent()
                && encoder.match(token, user.get().getPasswordResetToken())
                && user.get().getPasswordResetTokenExpiryDate().isBefore(LocalDateTime.now())){
            throw new BadRequestException("Password reset token expired");
        }else{
            throw new BadRequestException("Password reset token failed");
        }
    }

    public AuthenticationUser updateUserProfile(Long id, String firstName, String lastName, String location, String college) {

        Optional<AuthenticationUser> user = authenticationUserRepository.findById(id);
        if(user.isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }

        if(firstName!=null){
            user.get().setFirstName(firstName);
        }
        if(lastName!=null){
            user.get().setLastName(lastName);
        }
        if(location!=null){
            user.get().setLocation(location);
        }
        if(college!=null){
            user.get().setCollege(college);
        }

        authenticationUserRepository.save(user.get());
        return user.get();

    }

    @Transactional
    public void deleteUser(Long userId) {
        AuthenticationUser user = entityManager.find(AuthenticationUser.class,userId);
        if(user!=null){
            entityManager.createNativeQuery("DELETE FROM posts_likes WHERE user_id = :userId")
            .setParameter("userId",userId)
            .executeUpdate();
            entityManager.remove(user);
        }
    }
}
