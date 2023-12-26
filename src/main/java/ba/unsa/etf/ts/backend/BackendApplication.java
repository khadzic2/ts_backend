package ba.unsa.etf.ts.backend;

import ba.unsa.etf.ts.backend.model.Role;
import ba.unsa.etf.ts.backend.model.User;
import ba.unsa.etf.ts.backend.request.AddRoleRequest;
import ba.unsa.etf.ts.backend.request.AddUserRequest;
import ba.unsa.etf.ts.backend.services.RoleService;
import ba.unsa.etf.ts.backend.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner run(RoleService roleService, UserService userService) {
        return args->{
            //role
            Role adminId = roleService.addRole(new AddRoleRequest("ADMIN"));
            Role userId = roleService.addRole(new AddRoleRequest("USER"));

        //    User admin = userService.addUser(new AddUserRequest("admin","Admin123!@#","Admin","admin","admin@gmail.com",null, adminId.getId()));
        //    User khadzic = userService.addUser(new AddUserRequest("khadzic2","Khadzic2509!@#","Kanita","Hadzic","khadzic2@etf.unsa.ba","062054882",userId.getId()));
        };
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
