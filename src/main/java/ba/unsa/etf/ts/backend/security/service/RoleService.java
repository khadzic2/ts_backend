package ba.unsa.etf.ts.backend.security.service;

import ba.unsa.etf.ts.backend.request.AddRoleRequest;
import ba.unsa.etf.ts.backend.security.entity.Role;
import ba.unsa.etf.ts.backend.security.repository.RoleRepository;
import org.springframework.stereotype.Service;

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
