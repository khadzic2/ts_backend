package ba.unsa.etf.ts.backend.security.request;

import ba.unsa.etf.ts.backend.security.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse {
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("refreshToken")
    private String refreshToken;
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String phoneNumber;

    public AuthResponse() {
    }

    public AuthResponse(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id=user.getId();
        this.firstName = user.getFirstname();
        this.lastName = user.getLastname();
        this.role = user.getRole().getName();
        this.phoneNumber = user.getPhoneNumber();
        this.email= user.getEmail();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
}
