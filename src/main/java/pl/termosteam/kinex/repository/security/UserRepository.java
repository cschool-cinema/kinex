package pl.termosteam.kinex.repository.security;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.termosteam.kinex.domain.security.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    public User findByEmail(String usernameOrEmail);

    public User findByUsername(String usernameOrEmail);


    public boolean existsByUsername(String userName);

    public boolean existsByRole(String role);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE user u SET u.isEnabled = :isEnabled WHERE u.id = :userId")
    int updateIsEnabled(@Param("userId") int userId, @Param("isEnabled") boolean isEnabled);
}
