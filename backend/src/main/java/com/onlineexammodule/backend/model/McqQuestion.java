package com.onlineexammodule.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class McqQuestion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mcqId;
    private String questionText;
    private String correctAnswer;
    private String category;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "mcqQuestions")
    private List<Exam> exams=new ArrayList<>();
    
    
    @ManyToMany
    @JoinTable(
        name = "mcq_question_option",
        joinColumns = @JoinColumn(name = "mcq_id"),
        inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private List<QuestionOption> options=new ArrayList<>();  // List of options for MCQs

    public void removeExam(Exam exam){
        if(this.getExams().contains(exam))
        {
            this.getExams().remove(exam);
        }
     }
}
