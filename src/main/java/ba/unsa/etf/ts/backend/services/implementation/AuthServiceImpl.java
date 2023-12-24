package ba.unsa.etf.ts.backend.services.implementation;

import ba.unsa.etf.ts.backend.configurations.JwtUtil;
import ba.unsa.etf.ts.backend.model.*;
import ba.unsa.etf.ts.backend.repository.RoleRepository;
import ba.unsa.etf.ts.backend.repository.UserRepository;
import ba.unsa.etf.ts.backend.request.AddUserRequest;
import ba.unsa.etf.ts.backend.exception.ResourceNotFoundException;
import ba.unsa.etf.ts.backend.request.LoginOtpRequest;
import ba.unsa.etf.ts.backend.request.LoginRequest;
import ba.unsa.etf.ts.backend.response.LoginResponse;
import ba.unsa.etf.ts.backend.services.AuthService;
import ba.unsa.etf.ts.backend.utils.constants.Roles;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public User registerUserService(AddUserRequest user) throws MessagingException, UnsupportedEncodingException {

        User temp = userRepository.findByEmail(user.getEmail());
        if (temp != null) {
            throw new IllegalArgumentException("User with email: " + user.getEmail() + " already exists;");
        }

        Role role = getRole(user.getRole());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User newUser = new User(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber(),
                role);

        Pair<String, Date> otpPair = generateOneTimePassword();
        newUser.setOneTimePassword(passwordEncoder.encode(otpPair.getFirst()));
        newUser.setOtpGeneratedTime(otpPair.getSecond());
        newUser.setResetPassword(1);

        User savedUser = userRepository.save(newUser);
        sendOTPEmail(newUser, otpPair.getFirst());
        return savedUser;
    }

    @Override
    public User updateUser(AddUserRequest user) {

        if (user.getUserId() == -1) {
            throw new IllegalArgumentException("You must pass the id of the user you wish to update");
        }
        getUser(user.getUserId());
        Role role = getRole(user.getRole());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(new User(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber(),
                role));
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = getUser(userId);
        if (Objects.equals(user.getRole().getRoleName(), Roles.ADMIN.toString())) {
            throw new IllegalArgumentException("Can't delete the admin");
        }
        userRepository.delete(user);
    }

    public Role getRole(String roleName) {
        Optional<Role> role = roleRepository.findById(roleName);
        if (role.isEmpty()) {
            throw new ResourceNotFoundException("Role " + roleName + "doesn't exist");
        }
        return role.get();
    }

    public LoginResponse loginUserService(LoginRequest loginRequest) {
//        authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginRequest.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        return new LoginResponse(userRepository.findByEmail(loginRequest.getEmail()), token);
    }

    public void resetPassword(Integer userId, String password) {
        User user = getUser(userId);
        user.setPassword(passwordEncoder.encode(password));
        user.setResetPassword(0);
        userRepository.save(user);
        clearOTP(userId);
    }

//    public void requestPasswordChange()

    public LoginResponse loginOtpUserService(LoginOtpRequest loginOtpRequestDto) throws AccessDeniedException {
        User user = userRepository.findByEmail(loginOtpRequestDto.getEmail());
        if(user == null){
            throw new BadCredentialsException("User with email " + loginOtpRequestDto.getEmail() + "doesn't exit");
        }
        if(new Date().getTime() - user.getOtpGeneratedTime().getTime() > 3600000){
            throw new AccessDeniedException("OTP expired, please request a new one from the admin");
        }
        authenticate(loginOtpRequestDto.getEmail(), loginOtpRequestDto.getOtp());
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginOtpRequestDto.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        return new LoginResponse(user, token);
    }

    public Role getUserRoleByUserId(Integer userId) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get().getRole();
        } else {
            throw new ResourceNotFoundException("User with id " + userId + " doesn't exist");
        }
    }

    public User getUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User with id:" + userId + "doesn't exist");
        }
        return user.get();
    }

    @Override
    public User getUserFromToken() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void verifyUserRole(Integer userId, String roleName) throws AccessDeniedException {
        if (!getUserRoleByUserId(userId).getRoleName().equals(roleName)) {
            throw new AccessDeniedException("User must be a " + roleName);
        }
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new DisabledException("User " + username + " is disabled", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect email or password", e);
        }
    }

    @Override
    public Pair<String, Date> generateOneTimePassword() {
        String otp = RandomString.make(8);
        return Pair.of(otp, new Date());
    }

    @Override
    public void setNewOneTimePassword(Integer userId) throws MessagingException, UnsupportedEncodingException {
        User user = getUser(userId);
        if (user.getRole().getRoleName().equals(Roles.ADMIN.toString())) {
            throw new IllegalArgumentException("Can't generate new otp for admin");
        }
        Pair<String, Date> otpPair = generateOneTimePassword();
        user.setOneTimePassword(passwordEncoder.encode(otpPair.getFirst()));
        user.setOtpGeneratedTime(otpPair.getSecond());
        user.setResetPassword(1);
        userRepository.save(user);
        sendOTPEmail(user, otpPair.getFirst());
    }

    @Override
    public void sendOTPEmail(User user, String otp) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("tsexampotal@gmail.com", "Exam Portal");
        helper.setTo(user.getEmail());

        String subject = "Here's your One Time Password (OTP) - Expires in 60 minutes!";

        String content = "<p>Hello " + user.getFullName() + "</p>"
                + "<p>For security reason, you're required to use the following "
                + "One Time Password to login:</p>"
                + "<p><b>" + otp + "</b></p>"
                + "<br>"
                + "<p>Note: this OTP is set to expire in 60 minutes.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @Override
    public void sendChangePasswordEmail(User user, String passwordChangeCode) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("tsexampotal@gmail.com", "Exam Portal");
        helper.setTo(user.getEmail());

        String subject = "Here's your Link for password change - Expires in 5 minutes!";

        String content = "<p>Hello " + user.getFullName() + "</p>"
                + "<p>For security reason, you're required to use the following link to change your password!</p>"
                + "<a target='_blank' href=\"http://localhost:3000/resetPassword/" + passwordChangeCode + "\""
                + "<button> Change your password </button>"
                + "</form>"
                + "</a>"
                + "<br>"
                + "<p>Note: this link is set to expire in 5 minutes.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void changePasswordRequest(Integer userId) throws MessagingException, UnsupportedEncodingException {
        Pair<String, Date> changePasswordCode = generateOneTimePassword();
        User user = getUser(userId);
        user.setChangePasswordCode(passwordEncoder.encode(changePasswordCode.getFirst()));
        user.setChangePasswordGeneratedTime(changePasswordCode.getSecond());
        userRepository.save(user);
        sendChangePasswordEmail(user, changePasswordCode.getFirst());
    }

    public void changePassword(Integer userId, String password, String passwordCode) throws AccessDeniedException {
        User user = getUser(userId);
        if(!passwordEncoder.matches(passwordCode, user.getChangePasswordCode())){
            throw new AccessDeniedException("Invalid code for password change");
        }
        if(new Date().getTime() - user.getChangePasswordGeneratedTime().getTime() > 300000){
            throw new AccessDeniedException("The link has expired");
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setChangePasswordCode(null);
        user.setChangePasswordGeneratedTime(null);
        userRepository.save(user);
    }

    @Override
    public void clearOTP(Integer userId) {
        User user = getUser(userId);
        user.setOneTimePassword(null);
        user.setOtpGeneratedTime(null);
        userRepository.save(user);
    }
}