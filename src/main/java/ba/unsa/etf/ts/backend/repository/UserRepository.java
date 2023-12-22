package ba.unsa.etf.ts.backend.repository;

import ba.unsa.etf.ts.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserByUsername(String username);
}
