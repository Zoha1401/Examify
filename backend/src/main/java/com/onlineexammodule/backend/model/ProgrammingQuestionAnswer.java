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
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProgrammingQuestionAnswer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programmingAnswerId;
    
    private String language;

    private Long pqId;
    
    
    private String codeSubmission;

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    @JsonBackReference("programmingReference")
    private Answer answer;


}
