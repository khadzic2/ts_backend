package ba.unsa.etf.ts.backend.security.repository;

import ba.unsa.etf.ts.backend.security.entity.PasswordResetToken;
import ba.unsa.etf.ts.backend.security.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    public PasswordResetToken findByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE (t.expiryDate < :currentDateAndTime)")
    void deleteExpiredTokens(@Param("currentDateAndTime") LocalDateTime currentDateAndTime);


    @Transactional
    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.user = :user")
    void deleteByUser(@Param("user") User user);
}
