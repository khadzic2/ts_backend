package ba.unsa.etf.ts.backend.security.service;

import ba.unsa.etf.ts.backend.exception.BadRequestException;
import ba.unsa.etf.ts.backend.exception.NotFoundException;
import ba.unsa.etf.ts.backend.security.request.AddUserRequest;
import ba.unsa.etf.ts.backend.security.request.UpdateUserRequest;

import ba.unsa.etf.ts.backend.security.entity.Role;
import ba.unsa.etf.ts.backend.security.entity.User;
import ba.unsa.etf.ts.backend.security.repository.RoleRepository;
import ba.unsa.etf.ts.backend.security.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User addUser(AddUserRequest addUserRequest) throws BadRequestException {
        User user2 = userRepository.findByEmail(addUserRequest.getEmail()).orElse(null);
        if(user2 != null){
            throw new BadRequestException("Email "+addUserRequest.getEmail()+" already exist.");
        }

        Role role = roleRepository.findById(addUserRequest.getRoleId()).orElseThrow(()->new NotFoundException("Role by id:"+addUserRequest.getRoleId()+" does not exist."));
        User user = new User();
        user.setFirstname(addUserRequest.getFirstName());
        user.setLastname(addUserRequest.getLastName());
        user.setEmail(addUserRequest.getEmail());
        user.setPhoneNumber(addUserRequest.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));
        user.setRole(role);

        return userRepository.save(user);
    }

    public User getUser(Integer id){
        return userRepository.findById(id).orElseThrow(()->new NotFoundException("User by id:"+id+" does not exist."));
    }

//    public User getUserByUsername(String username){
//        User user = userRepository.findByUsername(username);
//        if (user == null) throw new NotFoundException("User by username:"+username+" does not exist.");
//        return user;
//    }

    public User updateUser(Integer id, UpdateUserRequest newUser){
        User updateUser = userRepository.findById(id).orElseThrow(()->new NotFoundException("User by id:"+id+" does not exist."));
        updateUser.setFirstname(newUser.getFirstName());
        updateUser.setLastname(newUser.getLastName());
        updateUser.setEmail(newUser.getEmail());
        updateUser.setPhoneNumber(newUser.getPhoneNumber());

        return userRepository.save(updateUser);
    }

    public String deleteUser(Integer id){
        User user = userRepository.findById(id).orElse(null);
        if(user == null) throw new NotFoundException("User by id:"+id+" does not exist.");
        userRepository.deleteById(id);
        return "User successfully deleted!";
    }
}