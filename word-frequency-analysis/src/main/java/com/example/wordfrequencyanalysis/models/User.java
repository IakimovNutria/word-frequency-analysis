package com.example.wordfrequencyanalysis.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;
import java.util.Map;

@Entity
public class User {
    @Id
    private long userId;

    @OneToMany
    private List<Word> words;
}
