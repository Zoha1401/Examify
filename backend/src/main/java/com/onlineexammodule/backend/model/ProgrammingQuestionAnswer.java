package com.onlineexammodule.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
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
    
    @Transient
    private Long programmingQuestionId;
    
    @ManyToOne
    @JoinColumn(name="programming_question_id", nullable = false)
    private ProgrammingQuestion programmingQuestion;

    private String codeSubmission;

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;


}
