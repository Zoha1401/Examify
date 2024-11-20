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
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class McqAnswer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long mcqAnswerId;
    
    @Transient
    private Long mcqQuestionId;

    @Transient
    private Long selectedOptionId;
    
    @ManyToOne
    @JoinColumn(name = "mcq_id", nullable = false)
    private McqQuestion mcqQuestion; 

    @ManyToOne
    @JoinColumn(name="option_id")
    private QuestionOption selectedQuestionOption;

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;
}
