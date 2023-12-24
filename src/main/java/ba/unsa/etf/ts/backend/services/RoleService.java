package ba.unsa.etf.ts.backend.services;

import ba.unsa.etf.ts.backend.model.Role;
import ba.unsa.etf.ts.backend.repository.RoleRepository;
import ba.unsa.etf.ts.backend.request.AddRoleRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role addRole(AddRoleRequest addRoleRequest){
        Role role = new Role();
        role.setName(addRoleRequest.getName());
        return roleRepository.save(role);
    }
}
