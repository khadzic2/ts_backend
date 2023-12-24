package ba.unsa.etf.ts.backend.services.implementation;

import ba.unsa.etf.ts.backend.model.User;
import ba.unsa.etf.ts.backend.repository.UserRepository;
import ba.unsa.etf.ts.backend.services.AuthService;
import ba.unsa.etf.ts.backend.services.UserService;
import ba.unsa.etf.ts.backend.utils.constants.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Override
    public List<User> getAllUsers(Integer adminId) throws AccessDeniedException {
        authService.verifyUserRole(adminId, Roles.ADMIN.toString());
        return userRepository.findAll();
    }

}