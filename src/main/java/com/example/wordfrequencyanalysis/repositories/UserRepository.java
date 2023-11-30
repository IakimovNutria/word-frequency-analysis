package com.example.wordfrequencyanalysis.repositories;

import com.example.wordfrequencyanalysis.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findUserById(long id);
}
