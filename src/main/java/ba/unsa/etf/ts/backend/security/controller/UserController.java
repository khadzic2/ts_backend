package ba.unsa.etf.ts.backend.security.controller;

import ba.unsa.etf.ts.backend.exception.BadRequestException;
import ba.unsa.etf.ts.backend.security.request.AddUserRequest;
import ba.unsa.etf.ts.backend.security.request.UpdateUserRequest;
import ba.unsa.etf.ts.backend.response.ErrorResponse;
import ba.unsa.etf.ts.backend.security.service.JwtService;
import ba.unsa.etf.ts.backend.security.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Object> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer id){
        return ResponseEntity.ok(userService.getUser(id));
    }
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/details")
    public ResponseEntity<Object> getUserByEmail(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        String email = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping
    public ResponseEntity<Object> updateUserById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid UpdateUserRequest newUser){
        String email = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(userService.updateUser(email,newUser));
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Valid AddUserRequest addUserRequest){
        try {
            return new ResponseEntity<>(userService.addUser(addUserRequest), HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Already exist",e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping
    public ResponseEntity<Object> deleteUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        String email = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(userService.deleteUser(email));
    }
}
