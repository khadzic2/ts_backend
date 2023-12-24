package ba.unsa.etf.ts.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ba.unsa.etf.ts.backend.utils.validation.annotation.UniqueEmailConstraint;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "users") //annotations
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer id;

    @NotEmpty(message = "First name is required")
    @Size(max = 50, message = "First name can't have more than 50 characters")
    @NotNull(message = "First name is required")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    @Size(max = 50, message = "Last name can't have more than 50 characters")
    @NotNull(message = "Last name is required")
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty(message = "Email is required!")
    @Column(name = "username", unique = true)
    @NotNull(message = "Email is required")
    @Email(message = "Wrong email format")
    @UniqueEmailConstraint
    private String email;


    @NotEmpty(message = "Password is required!")
    @Size(min = 8, message = "Password can't be less than 8 characters!")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain at least one uppercase letter and one digit!"
    )
    @Column(name = "password")
    @JsonIgnore
    private String password;


    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_active")
    private boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "user_role",referencedColumnName = "role_name")
    @NotNull(message = "User must have a role")
    private Role role;

    @Column(name = "one_time_password")
    @JsonIgnore
    private String oneTimePassword;

    @Column(name = "otp_generated_time")
    private Date otpGeneratedTime;

    @Column(name = "change_password_code")
    @JsonIgnore
    private String changePasswordCode;

    @Column(name = "reset_password_generated_time")
    private Date changePasswordGeneratedTime;

    @Column(name = "set_password")
    private int resetPassword;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Usercart> cart;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Orders> orders;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        return authorities;
    }

    @Override
    public String getPassword(){
        if(isOTPRequired()){
            return oneTimePassword;
        }
        return password;
    }

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public User(String firstName, String lastName, String email, String password,
                String phoneNumber, Role userRole) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.email = email;
        this.password=password;
        this.phoneNumber=phoneNumber;
        this.role=userRole;
    }

    public User(Integer id, String firstName, String lastName, String email, String password,
                String phoneNumber, Role userRole) {
        this.id = id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email = email;
        this.password=password;
        this.phoneNumber=phoneNumber;
        this.role=userRole;
    }

    public boolean isOTPRequired(){
        return this.getOneTimePassword() != null;
    }

}
