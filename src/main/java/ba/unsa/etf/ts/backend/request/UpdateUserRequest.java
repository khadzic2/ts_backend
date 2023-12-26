package ba.unsa.etf.ts.backend.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserRequest {
    @NotBlank (message = "First name must be specified")
    private String firstName;
    @NotBlank (message = "Last name must be specified")
    private String lastName;
    @NotBlank(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;
    @Pattern(regexp = "[0-9]+")
    private String phoneNumber;
}
