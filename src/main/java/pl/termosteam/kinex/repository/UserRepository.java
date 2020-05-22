package pl.termosteam.kinex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.termosteam.kinex.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByRole(String role);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.isEnabled = :isEnabled WHERE u.id = :userId")
    int updateIsEnabled(@Param("userId") int userId, @Param("isEnabled") boolean isEnabled);
}
