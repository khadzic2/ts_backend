package ba.unsa.etf.ts.backend.security.controller;

import ba.unsa.etf.ts.backend.security.request.*;
import ba.unsa.etf.ts.backend.security.service.AuthenticationService;
import ba.unsa.etf.ts.backend.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


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

    @PreAuthorize("hasAuthority('USER')")
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
}
