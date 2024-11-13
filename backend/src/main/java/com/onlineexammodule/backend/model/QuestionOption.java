package com.onlineexammodule.backend.model;


import com.fasterxml.jackson.annotation.JsonBackReference;


import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;  

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "mcq_id")
    private McqQuestion mcqQuestion;


    private String optionText;  
    private boolean isCorrect;
   
}
