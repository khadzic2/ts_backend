package ba.unsa.etf.ts.backend.security.service;

import ba.unsa.etf.ts.backend.security.repository.RoleRepository;
import ba.unsa.etf.ts.backend.security.repository.UserRepository;
import ba.unsa.etf.ts.backend.security.request.AuthCredentials;
import ba.unsa.etf.ts.backend.security.request.AuthResponse;
import ba.unsa.etf.ts.backend.security.entity.User;
import ba.unsa.etf.ts.backend.security.token.Token;
import ba.unsa.etf.ts.backend.security.token.TokenRepository;
import ba.unsa.etf.ts.backend.security.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class AuthenticationService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    private void saveToken(User savedUser, String jwtToken) {
        var token = new Token(jwtToken, TokenType.BEARER,false,false, savedUser);
        tokenRepository.save(token);
    }

    public AuthResponse authenticate(AuthCredentials authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(),
                    authRequest.getPassword()));

        var user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow();
        var role = user.getRole();
        Map<String,Object> roleMap= new HashMap<>();
        roleMap.put("Role",role.getName());
        roleMap.put("id",user.getId());
        roleMap.put("firstName",user.getFirstname());
        var jwtToken = jwtService.generateToken(roleMap,user);
        var refreshToken = jwtService.generateRefreshToken(roleMap,user);
        revokeAllUserTokens(user);
        saveToken(user, jwtToken);
        return  new AuthResponse(jwtToken,refreshToken,user);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if(validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if(userEmail!=null) {
            var userDetails = this.userRepository.findByEmail(userEmail).orElseThrow();
            var role = userDetails.getRole();
            Map<String,Object> roleMap= new HashMap<>();
            roleMap.put("Role",role.getName());
            roleMap.put("id",userDetails.getId());
            roleMap.put("firstName",userDetails.getFirstname());
            if(jwtService.isTokenValid(refreshToken,userDetails)) {
                var accessToken = jwtService.generateToken(roleMap,userDetails);
                revokeAllUserTokens(userDetails);
                saveToken(userDetails, accessToken);
                return new AuthResponse(accessToken,refreshToken,userDetails);
            }
        }

        return null;
    }

    public boolean compareHash(String providedPassword, String email) {
        UserDetails userDetails = userRepository.findByEmail(email).orElseThrow();


        return passwordEncoder.matches(providedPassword,userDetails.getPassword());
    }

    public String getHash(String password) {
        return passwordEncoder.encode(password);
    }

    public void changePassword(String email, String newPassword) {
        var user = userRepository.findByEmail(email).orElseThrow();
        user.setPassword(getHash(newPassword));
        userRepository.save(user);
    }
}
