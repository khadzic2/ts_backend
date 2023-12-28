package ba.unsa.etf.ts.backend.security.request;

public class ResetPasswordWithTokenRequest {

    private String token;

    private String password;

    public ResetPasswordWithTokenRequest(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
