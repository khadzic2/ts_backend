package ba.unsa.etf.ts.backend.repository;

import ba.unsa.etf.ts.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
}
