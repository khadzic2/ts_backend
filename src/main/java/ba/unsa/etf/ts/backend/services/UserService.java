package ba.unsa.etf.ts.backend.services;

import ba.unsa.etf.ts.backend.exception.NotFoundException;
import ba.unsa.etf.ts.backend.model.Role;
import ba.unsa.etf.ts.backend.model.User;
import ba.unsa.etf.ts.backend.repository.RoleRepository;
import ba.unsa.etf.ts.backend.repository.UserRepository;
import ba.unsa.etf.ts.backend.request.AddUserRequest;
import ba.unsa.etf.ts.backend.request.UpdateUserRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User addUser(AddUserRequest addUserRequest){
        Role role = roleRepository.findById(addUserRequest.getRoleId()).orElseThrow(()->new NotFoundException("Role by id:"+addUserRequest.getRoleId()+" does not exist."));
        User user = new User();
        user.setFirstName(addUserRequest.getFirstName());
        user.setLastName(addUserRequest.getLastName());
        user.setEmail(addUserRequest.getEmail());
        user.setPhoneNumber(addUserRequest.getPhoneNumber());
        user.setPassword(addUserRequest.getPassword());
        user.setUsername(addUserRequest.getUsername());
        user.setRole(role);

        return userRepository.save(user);
    }

    public User getUser(Integer id){
        return userRepository.findById(id).orElseThrow(()->new NotFoundException("User by id:"+id+" does not exist."));
    }

    public User getUserByUsername(String username){
        User user = userRepository.findUserByUsername(username);
        if (user == null) throw new NotFoundException("User by username:"+username+" does not exist.");
        return user;
    }

    public User updateUser(Integer id, UpdateUserRequest newUser){
        User updateUser = userRepository.findById(id).orElseThrow(()->new NotFoundException("User by id:"+id+" does not exist."));
        updateUser.setUsername(newUser.getUsername());
        updateUser.setPassword(newUser.getPassword());
        updateUser.setFirstName(newUser.getFirstName());
        updateUser.setLastName(newUser.getLastName());
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

    public String deleteAll(){
        userRepository.deleteAll();
        return "Successfully deleted!";
    }
}
