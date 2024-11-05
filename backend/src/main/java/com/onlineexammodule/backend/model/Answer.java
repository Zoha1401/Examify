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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId; 

    @ManyToOne
    @JoinColumn(name = "exam_result_id")
    private Result examResult;  

    @ManyToOne
    @JoinColumn(name = "examinee_id")
    private Examinee examinee;  

    @ManyToOne
    @JoinColumn(name = "mcq_question_id", nullable = true)
    private Mcq mcqQuestion;  

    @ManyToOne
    @JoinColumn(name = "programming_question_id", nullable = true)
    private Programming programmingQuestion;  

    @ManyToOne
    @JoinColumn(name = "selected_option_id", nullable = true)
    private Option selectedOption;  

    private String codeSubmission;  
}
