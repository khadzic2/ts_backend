package ba.unsa.etf.ts.backend.response;

import ba.unsa.etf.ts.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private User user;
    private String jwtToken;
}
