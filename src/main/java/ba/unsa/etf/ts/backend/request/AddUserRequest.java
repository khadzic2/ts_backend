package ba.unsa.etf.ts.backend.request;

import ba.unsa.etf.ts.backend.model.User;
import ba.unsa.etf.ts.backend.utils.validation.annotation.UniqueEmailConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
public class AddUserRequest {

    private Integer userId = -1;

    @NotEmpty(message = "First name is required")
    @Size(max = 50, message = "First name can't have more than 50 characters")
    @NotNull(message = "First name is required")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    @Size(max = 50, message = "Last name can't have more than 50 characters")
    @NotNull(message = "Last name is required")
    private String lastName;

    @NotEmpty(message = "Email is required!")
    @NotNull(message = "Email is required")
    @Email(message = "Wrong email format")
    @UniqueEmailConstraint
    private String email;

    private String password = "TESt123?";

    private String phoneNumber;

    @NotNull(message = "User must have a role")
    private String role;

    public AddUserRequest(User user){
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole().getRoleName();
    }

}
