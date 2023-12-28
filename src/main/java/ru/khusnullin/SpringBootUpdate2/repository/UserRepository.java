package ru.khusnullin.SpringBootUpdate2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khusnullin.SpringBootUpdate2.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmail(String email);
}
