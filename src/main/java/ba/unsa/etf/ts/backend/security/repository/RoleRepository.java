package ba.unsa.etf.ts.backend.security.repository;

import ba.unsa.etf.ts.backend.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
