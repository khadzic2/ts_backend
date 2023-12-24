package ba.unsa.etf.ts.backend.repository;

import ba.unsa.etf.ts.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,String> {
    Role findByRoleName(String roleName);
}
