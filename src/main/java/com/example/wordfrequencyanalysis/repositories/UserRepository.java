package com.example.wordfrequencyanalysis.repositories;

import com.example.wordfrequencyanalysis.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(long id);
}
