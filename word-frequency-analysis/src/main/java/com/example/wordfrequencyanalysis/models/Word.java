package com.example.wordfrequencyanalysis.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Word {
    @Id
    @GeneratedValue
    private Long id;

    private String word;
    private Integer count;
}
