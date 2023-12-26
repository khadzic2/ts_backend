package ba.unsa.etf.ts.backend.controller;

import ba.unsa.etf.ts.backend.exception.NotFoundException;
import ba.unsa.etf.ts.backend.model.User;
import ba.unsa.etf.ts.backend.request.AddUserRequest;
import ba.unsa.etf.ts.backend.request.ChangePasswordRequest;
import ba.unsa.etf.ts.backend.request.UpdateUserRequest;
import ba.unsa.etf.ts.backend.response.ErrorResponse;
import ba.unsa.etf.ts.backend.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping(path = "/user/{username}")
    public ResponseEntity<Object> getUserByUsername(Authentication authentication, @PathVariable String username){
        String usernameFromToken = authentication.getName();
        if (!usernameFromToken.equals(username)) {
            ErrorResponse errorResponse = new ErrorResponse("forbidden", "Pogresan username");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        return ResponseEntity.ok().body(userService.getUser(username));
    }

    @PostMapping("/user")
    public ResponseEntity<Object> addUser(@RequestBody @Valid AddUserRequest addUserRequest){
        try {
            return new ResponseEntity<>(userService.addUser(addUserRequest), HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Already exist",e.getMessage()));
        }
    }

    @PostMapping("/user/{username}")
    public ResponseEntity<Object> updateUser (Authentication authentication, @RequestBody UpdateUserRequest newUser, @PathVariable String username) {
        String usernameFromToken = authentication.getName();
        if (!usernameFromToken.equals(username)) {
            ErrorResponse errorResponse = new ErrorResponse("forbidden", "Pogresan username");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        User user =  userService.updateUser(newUser, username);
        return ResponseEntity.ok(user);
    }
    @PostMapping(path = "/password-change")
    public ResponseEntity<Object> changePassword(Authentication authentication,@RequestBody ChangePasswordRequest request){
        String username = authentication.getName();
        User user;
        try {
            user = userService.changePassword(username, request);
        } catch (NotFoundException e){
            ErrorResponse errorResponse = new ErrorResponse("not valid", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        return ResponseEntity.ok().body(user);
    }

    @GetMapping(path = "/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader  = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm  = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user  = userService.getUser(username);

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("role",user.getRole().getName())
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", access_token);
                tokens.put("refreshToken", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception exception) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                System.out.println("ovdje");
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }

        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
