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
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class McqAnswer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mcqAnswerId;
    
    private Long mcqQuestionId;

    private Long selectedOptionId;

    private boolean isCorrect;
   
    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    @JsonBackReference("mcqAnswerReference")
    private Answer answer;
}
