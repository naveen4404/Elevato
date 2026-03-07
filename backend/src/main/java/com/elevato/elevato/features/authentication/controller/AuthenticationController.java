package com.elevato.elevato.features.authentication.controller;

import com.elevato.elevato.exception.ForbiddenException;
import com.elevato.elevato.features.authentication.dto.AuthenticationUserRequest;
import com.elevato.elevato.features.authentication.dto.AuthenticationUserResponse;
import com.elevato.elevato.features.authentication.model.AuthenticationUser;
import com.elevato.elevato.features.authentication.service.AuthenticationService;
import com.elevato.elevato.utils.SuccessResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;



    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @GetMapping("/user")
    public  AuthenticationUser getUser(@RequestAttribute("authenticatedUser") AuthenticationUser user){
        return user;
    }

    @DeleteMapping("/user")
    public ResponseEntity<Void> deleteUser(@RequestAttribute("authenticatedUser") AuthenticationUser user){

        authenticationService.deleteUser(user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public AuthenticationUserResponse register(@Valid @RequestBody AuthenticationUserRequest newUser) throws MessagingException, UnsupportedEncodingException {
        return authenticationService.register(newUser);
    }

    @PostMapping("/login")
    public AuthenticationUserResponse login(@Valid @RequestBody AuthenticationUserRequest loginUser){
        return authenticationService.login(loginUser);
    }

    @GetMapping("/send-email-verification-token")
    public ResponseEntity<SuccessResponse> sendEmailVerificationToken(@RequestAttribute("authenticatedUser") AuthenticationUser user){
        authenticationService.sendEmailVerificationToken(user.getEmail());
        return ResponseEntity.ok(new SuccessResponse("Email verification token sent successfully.", HttpStatus.OK.value()));
    }

    @PutMapping("/validate-email-verification-token")
    public ResponseEntity<SuccessResponse> verifyEmail(@RequestParam String token, @RequestAttribute("authenticatedUser") AuthenticationUser user){
        authenticationService.validateEmailVerificationToken(token, user.getEmail());
        return ResponseEntity.ok(new SuccessResponse("Email verified successfully.", HttpStatus.OK.value()));
    }

    @PutMapping("/send-password-reset-token")
    public ResponseEntity<SuccessResponse> sendPasswordResetToken(@RequestParam String email){
        authenticationService.sendPasswordResetToken(email);
        return ResponseEntity.ok(new SuccessResponse("password reset token sent successfully.", HttpStatus.OK.value()));

    }

    @PutMapping("/reset-password")
    public ResponseEntity<SuccessResponse> resetPassword(@RequestParam String newPassword, @RequestParam String token, @RequestParam String email){
        authenticationService.resetPassword(email,newPassword,token);
        return ResponseEntity.ok(new SuccessResponse("Password reset successful.", HttpStatus.OK.value()));

    }
    @PutMapping("/profile/{id}")
    public ResponseEntity<AuthenticationUser>  updateUserProfile(@RequestAttribute("authenticatedUser") AuthenticationUser user,
                                                 @PathVariable Long id,
                                                 @RequestParam(required = false) String firstName,
                                                 @RequestParam(required = false) String lastName,
                                                 @RequestParam(required = false) String location,
                                                 @RequestParam(required = false) String college

                                                 ){
        if(!user.getId().equals(id)){
            throw  new ForbiddenException("Your are not authorized to do this");
        }

        return ResponseEntity.ok(authenticationService.updateUserProfile(id, firstName, lastName,location,college));
    }

}
