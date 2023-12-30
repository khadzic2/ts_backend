package ba.unsa.etf.ts.backend.security.email;

public interface EmailSender {
    void send(String to, String email);
}
