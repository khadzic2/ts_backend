package ba.unsa.etf.ts.backend.security.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserRequest {

    @NotBlank (message = "First name must be specified")
    private String firstName;
    @NotBlank (message = "Last name must be specified")
    private String lastName;
    @Pattern(regexp = "[0-9]+")
    private String phoneNumber;
}
