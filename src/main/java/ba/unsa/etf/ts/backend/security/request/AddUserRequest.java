package ba.unsa.etf.ts.backend.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddUserRequest {

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
