package ba.unsa.etf.ts.backend.services;


import ba.unsa.etf.ts.backend.model.User;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;


public interface UserService {
    List<User> getAllUsers(Integer adminId) throws AccessDeniedException;

}
