package com.example.wordfrequencyanalysis.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Group {
    @Id
    private long groupId;

    @OneToMany
    private List<User> users;

    @OneToMany
    private List<Word> words;
}
