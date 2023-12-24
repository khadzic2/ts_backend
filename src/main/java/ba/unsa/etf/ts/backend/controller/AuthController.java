package ba.unsa.etf.ts.backend.controller;

import ba.unsa.etf.ts.backend.repository.UserRepository;
import ba.unsa.etf.ts.backend.request.LoginOtpRequest;
import ba.unsa.etf.ts.backend.request.LoginRequest;
import ba.unsa.etf.ts.backend.response.LoginResponse;
import ba.unsa.etf.ts.backend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody LoginRequest loginRequest) {
        return authService.loginUserService(loginRequest);
    }

    @PostMapping("/login-otp")
    public LoginResponse loginUserOTP(@RequestBody LoginOtpRequest loginOtpRequest) throws AccessDeniedException {
        return authService.loginOtpUserService(loginOtpRequest);
    }

}
