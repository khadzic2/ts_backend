package ba.unsa.etf.ts.backend.security.controller;

import ba.unsa.etf.ts.backend.exception.BadRequestException;
import ba.unsa.etf.ts.backend.security.request.AddUserRequest;
import ba.unsa.etf.ts.backend.security.request.UpdateUserRequest;
import ba.unsa.etf.ts.backend.response.ErrorResponse;
import ba.unsa.etf.ts.backend.security.service.UserService;
import jakarta.validation.Valid;
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

    public UserController(UserService userService) {
        this.userService = userService;
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

//    @GetMapping("/username/{username}")
//    public ResponseEntity<Object> getUserByUsername(@PathVariable String username){
//        return ResponseEntity.ok(userService.getUserByUsername(username));
//    }
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUserById(@PathVariable Integer id, @RequestBody @Valid UpdateUserRequest newUser){
        return ResponseEntity.ok(userService.updateUser(id,newUser));
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Integer id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
