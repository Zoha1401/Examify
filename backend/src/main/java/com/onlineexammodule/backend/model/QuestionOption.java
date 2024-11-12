package com.onlineexammodule.backend.model;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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

    
    @ManyToMany(mappedBy = "options")
    private List<McqQuestion> mcqQuestions=new ArrayList<>(); 

    private String optionText;  
    private boolean isCorrect;
    public void addMcqQuestion(McqQuestion mcqQuestion) {
        if(!this.getMcqQuestions().contains(mcqQuestion))
        {
            this.getMcqQuestions().add(mcqQuestion);
        }
    }  
}
