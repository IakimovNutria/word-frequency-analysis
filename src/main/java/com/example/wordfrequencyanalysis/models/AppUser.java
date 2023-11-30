package com.example.wordfrequencyanalysis.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class AppUser {
    @Id
    long id;

    // TODO: разобраться можно ли убрать (fetch = FetchType.EAGER)
    @OneToMany(fetch = FetchType.EAGER)
    List<Word> words;

    public AppUser() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }
}
