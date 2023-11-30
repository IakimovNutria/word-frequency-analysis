package com.example.wordfrequencyanalysis.repositories;

import com.example.wordfrequencyanalysis.models.Group;
import com.example.wordfrequencyanalysis.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findGroupById(long id);
}
