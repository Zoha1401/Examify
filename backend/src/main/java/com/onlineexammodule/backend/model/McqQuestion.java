package com.onlineexammodule.backend.model;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
    @ManyToMany(mappedBy = "mcqQuestions", fetch = FetchType.LAZY)
    private List<Exam> exams=new ArrayList<>();
    
 
    @OneToMany(mappedBy = "mcqQuestion")
    @JsonManagedReference
    private List<QuestionOption> options = new ArrayList<>() ;// List of options for MCQs

    public void removeExam(Exam exam){
        if(this.getExams().contains(exam))
        {
            this.getExams().remove(exam);
        }
     }
}
