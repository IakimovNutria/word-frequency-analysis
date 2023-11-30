package com.example.wordfrequencyanalysis.repositories;

import com.example.wordfrequencyanalysis.models.AppGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<AppGroup, Long> {
    AppGroup findGroupById(long id);
}
