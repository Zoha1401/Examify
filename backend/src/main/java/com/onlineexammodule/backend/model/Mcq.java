package com.onlineexammodule.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mcq {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mcqId;
    private String questionText;
    private String options;
    private String answer;


    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;
}
