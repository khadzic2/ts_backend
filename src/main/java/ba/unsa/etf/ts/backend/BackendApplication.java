package ba.unsa.etf.ts.backend;

import ba.unsa.etf.ts.backend.request.AddRoleRequest;
import ba.unsa.etf.ts.backend.security.request.AddUserRequest;
import ba.unsa.etf.ts.backend.security.repository.PasswordResetTokenRepository;
import ba.unsa.etf.ts.backend.security.token.TokenRepository;
import ba.unsa.etf.ts.backend.security.service.RoleService;
import ba.unsa.etf.ts.backend.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@EnableJpaRepositories(basePackages = { "ba.unsa.etf.ts.backend.repository","ba.unsa.etf.ts.backend.security.repository","ba.unsa.etf.ts.backend.security.token"})
@EntityScan(basePackages = {"ba.unsa.etf.ts.backend.model","ba.unsa.etf.ts.backend.request","ba.unsa.etf.ts.backend.security.entity","ba.unsa.etf.ts.backend.security.token"})
@EnableScheduling
@SpringBootApplication
public class BackendApplication implements CommandLineRunner{
    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    TokenRepository tokenRepository;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
    @Override
    public void run(String... args)
    {
        addRolesToDatabase();
        addAdminToDatabase();
    }

    private void addAdminToDatabase() {
        userService.addUser(new AddUserRequest("Admin123!@#","Admin","admin","admin@gmail.com",null, 1));
    }

    private void addRolesToDatabase(){
        roleService.addRole(new AddRoleRequest("ADMIN"));
        roleService.addRole(new AddRoleRequest("USER"));
    }

    private void deleteExpiredTokens() {
        tokenRepository.deleteInvalidTokens();
    }

    private void deleteExpiredPasswordResetTokens() {
        passwordResetTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }

    @Scheduled(cron = "0 0 * * * *") // Cron expression for running every hour
    public void performTask() {
        System.out.println("Scheduled deletion of expired tokens.");
        deleteExpiredTokens();
        deleteExpiredPasswordResetTokens();
    }
}
