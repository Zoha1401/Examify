package com.onlineexammodule.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgrammingQuestion {
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programmingQuestionId;
    private String programmingQuestionText;

    @OneToMany(cascade = jakarta.persistence.CascadeType.ALL)
    @JoinColumn(name = "programming_question_id")
    private List<TestCase> testCases = new ArrayList<>();
    
    private String difficulty;
    private String reference_answer;

    @JsonIgnore
    @ManyToMany(mappedBy = "programmingQuestions")
    private List<Exam> exams = new ArrayList<>(); 

    public void removeExam(Exam exam) {
        this.exams.remove(exam);
    }

    public ProgrammingQuestion(String programmingQuestionText, List<TestCase> testCases, String difficulty) {
        this.programmingQuestionText = programmingQuestionText;
        this.difficulty = difficulty;
    }
}


