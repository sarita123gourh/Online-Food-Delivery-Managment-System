package com.db.repo;
import com.db.entities.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<Users,Long> {
    //Optional<Users> findByEmail(String email); // For authentication

    @Query("SELECT u FROM Users u") // Correct: returns a List<Users>
    List<Users> findAllUsers();

    Optional<Users> findByUsername(String username);

}
