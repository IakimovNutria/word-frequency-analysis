package com.example.wordfrequencyanalysis.models;

import jakarta.persistence.*;

@Entity
@Table(name = "word")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String word;

    @Column
    Integer count;

    public String getWord() {
        return word;
    }

    public Integer getCount() {
        return count;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
