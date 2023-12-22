package ba.unsa.etf.ts.backend;

import ba.unsa.etf.ts.backend.model.Role;
import ba.unsa.etf.ts.backend.request.AddRoleRequest;
import ba.unsa.etf.ts.backend.services.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner run(RoleService roleService){
        return args->{
            //role
            Role adminId = roleService.addRole(new AddRoleRequest("ADMIN"));
            Role userId = roleService.addRole(new AddRoleRequest("COSTUMER"));
        };
    }

//    @Bean
//    PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
}
