package ba.unsa.etf.ts.backend.security.entity;

import ba.unsa.etf.ts.backend.model.Cart;
import ba.unsa.etf.ts.backend.model.Orders;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ba.unsa.etf.ts.backend.security.token.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "firstname", columnDefinition = "VARCHAR(60)")
    @NotBlank (message = "Last name must be specified")
    @NotNull (message = "Last name must be specified")
    private String firstname;

    @NotBlank (message = "Last name must be specified")
    @NotNull (message = "Last name must be specified")
    @Column(name = "lastname", columnDefinition = "VARCHAR(60)")
    private String lastname;

    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(60)")
    @NotBlank(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "password", columnDefinition = "VARCHAR(60)")
    @JsonIgnore
    private String password;

    @Column(name = "phonenumber", columnDefinition = "VARCHAR(60)")
    @Pattern(regexp = "[0-9]+")
    private String phoneNumber;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Token> userTokens;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cart> carts;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Orders> orders;

    public User() {
    }

    public User(String firstname, String lastname, String email, String phoneNumber, String password, Role role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Token> getUserTokens() {
        return userTokens;
    }

    public void setUserTokens(List<Token> userTokens) {
        this.userTokens = userTokens;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = getRole();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return getUserPassword();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { // mozemo koristiti naknadno zasad hardkodirano
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // mozemo koristiti naknadno zasad hardkodirano
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // mozemo koristiti naknadno zasad hardkodirano
        return true;
    }

    @Override
    public boolean isEnabled() { // mozemo koristiti naknadno zasad hardkodirano
        return true;
    }
}
