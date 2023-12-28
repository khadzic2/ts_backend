package ba.unsa.etf.ts.backend.security.controller;

import ba.unsa.etf.ts.backend.security.repository.UserRepository;
import ba.unsa.etf.ts.backend.security.request.*;
import ba.unsa.etf.ts.backend.security.service.AuthenticationService;
import ba.unsa.etf.ts.backend.security.service.JwtService;
import ba.unsa.etf.ts.backend.security.service.PasswordResetTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*", methods = {RequestMethod.POST,RequestMethod.DELETE,RequestMethod.GET,RequestMethod.PUT})
@RestController
@RequestMapping(path="/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationService authenticationService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /*
    @GetMapping("/user")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        var user = authenticationService.getUserByEmail(email);
        if(user == null)
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);

        return  ResponseEntity.ok(user);
    }*/


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthCredentials credentials) {

        return ResponseEntity.ok(authenticationService.authenticate(credentials));

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response) {
        try {
            return ResponseEntity.ok(authenticationService.refreshToken(request,response));
        } catch (IOException e) {
            logger.error("Failed to refresh token");
            return (ResponseEntity<AuthResponse>) ResponseEntity.badRequest();
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        if(jwtService.validateToken(token)) {
            return ResponseEntity.ok("Token valid");
        } else {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You shall not pass!");
        }
    }

    @PutMapping(path="/change-password")
    public @ResponseBody
    ResponseEntity<String> changePassword(@Valid  @RequestBody ChangePasswordRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        var email = jwtService.extractUsername(token.substring(7));
        if(authenticationService.compareHash(request.getOldPassword(), email)) {
            authenticationService.changePassword(email,request.getNewPassword());
            return  ResponseEntity.ok("Successfully changed password");
        } else {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You shall not pass!");
        }
    }

    @PostMapping("/reset-password/request")
    public ResponseEntity<String> resetPasswordRequest(@RequestBody ResetPasswordRequest request) {

        try {
            passwordResetTokenService.resetPassword(request.getEmail());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User with this email does not exist!");
        }


        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Email with reset url sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordWithTokenRequest request) {
        String email = passwordResetTokenService.validatePasswordResetToken(request.getToken());


        if(email!=null) {
            authenticationService.changePassword(email, request.getPassword());

            return (ResponseEntity<String>) ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Password successfully changed!");
        } else {
            return (ResponseEntity<String>) ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid or expired token!");
        }
    }

}
