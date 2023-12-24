package ba.unsa.etf.ts.backend.controller;

import ba.unsa.etf.ts.backend.model.User;
import ba.unsa.etf.ts.backend.request.AddUserRequest;
import ba.unsa.etf.ts.backend.services.AuthService;
import ba.unsa.etf.ts.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> addUser(@Valid @RequestBody AddUserRequest addUserRequest ) throws AccessDeniedException, MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(authService.registerUserService(addUserRequest));
    }

    @GetMapping("/update")
    public ResponseEntity<User> updateUser(@Valid @RequestBody AddUserRequest addUserRequest) throws AccessDeniedException {
        return ResponseEntity.ok(authService.updateUser(addUserRequest));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) throws AccessDeniedException {
        authService.deleteUser(userId);
        return ResponseEntity.ok("User successfully deleted");
    }

    @PostMapping("/admin/generate-otp/{userId}")
    public ResponseEntity<String> generateOtp(@PathVariable Integer userId) throws MessagingException, UnsupportedEncodingException {
        authService.setNewOneTimePassword(userId);
        return ResponseEntity.ok("New OTP has been generated and sent to the user");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() throws AccessDeniedException {
        User admin = authService.getUserFromToken();
        System.out.println("ADMIN " + admin.getId());
        return ResponseEntity.ok(userService.getAllUsers(admin.getId()));
    }
}
