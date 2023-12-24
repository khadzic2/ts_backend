package ba.unsa.etf.ts.backend.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddUserRequest {
    @Pattern(regexp="^[A-z][A-z0-9-_]{3,23}$")
    @NotBlank(message = "Username must be specified")
    private String username;
    private String password;
    @NotBlank (message = "First name must be specified")
    private String firstName;
    @NotBlank (message = "Last name must be specified")
    private String lastName;
    @NotBlank(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;
    @Pattern(regexp = "[0-9]+")
    private String phoneNumber;

    private Integer roleId;
}
