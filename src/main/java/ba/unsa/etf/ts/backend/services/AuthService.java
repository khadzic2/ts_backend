package ba.unsa.etf.ts.backend.services;

import ba.unsa.etf.ts.backend.request.LoginOtpRequest;
import ba.unsa.etf.ts.backend.request.LoginRequest;
import ba.unsa.etf.ts.backend.response.LoginResponse;
import ba.unsa.etf.ts.backend.model.Role;
import ba.unsa.etf.ts.backend.model.User;
import ba.unsa.etf.ts.backend.request.AddUserRequest;
import org.springframework.data.util.Pair;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.Date;

public interface AuthService {
    User registerUserService(AddUserRequest user) throws MessagingException, UnsupportedEncodingException;

    User updateUser(AddUserRequest user);

    void deleteUser(Integer userId);

    Pair<String, Date> generateOneTimePassword();

    void setNewOneTimePassword(Integer userId) throws MessagingException, UnsupportedEncodingException;

    void sendOTPEmail(User user, String otp) throws MessagingException, UnsupportedEncodingException;

    void clearOTP(Integer userId);
    Role getUserRoleByUserId(Integer userId);

    User getUserFromToken();

    User getUser(Integer userId);

    void verifyUserRole(Integer userId, String roleName) throws AccessDeniedException;

    LoginResponse loginUserService(LoginRequest loginRequest);

    LoginResponse loginOtpUserService(LoginOtpRequest loginOtpRequest) throws AccessDeniedException;

    void resetPassword(Integer userId, String password);

    void changePasswordRequest(Integer userId) throws MessagingException, UnsupportedEncodingException;

    void changePassword(Integer userId, String password, String passwordCode) throws AccessDeniedException;

    void sendChangePasswordEmail(User user, String code) throws MessagingException, UnsupportedEncodingException;

}
