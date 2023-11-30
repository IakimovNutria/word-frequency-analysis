package com.example.wordfrequencyanalysis.repositories;

import com.example.wordfrequencyanalysis.models.User;
import com.example.wordfrequencyanalysis.models.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
    Word findWordById(long id);
}
