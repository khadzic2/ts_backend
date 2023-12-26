package ba.unsa.etf.ts.backend.services;

import ba.unsa.etf.ts.backend.exception.NotFoundException;
import ba.unsa.etf.ts.backend.model.Role;
import ba.unsa.etf.ts.backend.model.User;
import ba.unsa.etf.ts.backend.repository.RoleRepository;
import ba.unsa.etf.ts.backend.repository.UserRepository;
import ba.unsa.etf.ts.backend.request.AddRoleRequest;
import ba.unsa.etf.ts.backend.request.AddUserRequest;
import ba.unsa.etf.ts.backend.request.ChangePasswordRequest;
import ba.unsa.etf.ts.backend.request.UpdateUserRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public User addUser(AddUserRequest addUserRequest){
        User user1 = userRepository.findUserByUsername(addUserRequest.getUsername());
        if (user1!=null) {
            throw new NotFoundException("Vec postoji user sa poslanim username!");
        }
        User user2 = userRepository.findUserByEmail(addUserRequest.getEmail());
        if (user2!=null) {
            throw new NotFoundException("Vec postoji user sa poslanim email!");
        }
        User user = new User();
        user.setUsername(addUserRequest.getUsername());
        user.setPassword(addUserRequest.getPassword());
        user.setFirstName(addUserRequest.getFirstName());
        user.setLastName(addUserRequest.getLastName());
        user.setEmail(addUserRequest.getEmail());
        user.setPhoneNumber(addUserRequest.getPhoneNumber());
        //user.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));
        //user.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));
        //user.setPassword(addUserRequest.getPassword());
        user.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));
        user.setRole(roleRepository.findById(1).orElse(null));
        userRepository.save(user);
        return user;
    }
    public Role addRole(AddRoleRequest addRoleRequest) {
        Role role = new Role();
        role.setName(addRoleRequest.getName());

        roleRepository.save(role);
        return role;
    }
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user == null) {
            throw new NotFoundException("Ne postoji user sa poslanim username!");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getEnabled(),true, true, true, authorities);
    }

    public User getUser(String username) {
        return userRepository.findUserByUsername(username);
    }


    public int enableUser(String username) {
        return userRepository.enableUser(username);
    }

    public User updateUser(UpdateUserRequest newUser, String username) {
        User user = userRepository.findUserByUsername(username);
        if (user==null) {
            throw new NotFoundException("Ne postoji user sa poslanim username!");
        }
        if (newUser.getFirstName()!=null) {
            user.setFirstName(newUser.getFirstName());
        }
        if (newUser.getLastName()!=null) {
            user.setLastName(newUser.getLastName());
        }
        userRepository.save(user);
        return user;

    }

    public User changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findUserByUsername(username);

        if (user==null) {
            throw new NotFoundException("Ne postoji user sa poslanim username!");
        }
        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            //if (passwordEncoder.encode(request.getOldPassword()).equals(user.getPassword())){
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new NotFoundException("Pogresan old password");
        }
        return user;
    }
}