package com.onlineexammodule.backend.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CascadeType;

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
    private Long questionId;
    private String questionText;

    @OneToMany(cascade = jakarta.persistence.CascadeType.ALL)
    @JoinColumn(name = "programming_question_id")
    private List<TestCase> testCases = new ArrayList<>();
    
    private String difficulty_level;

    @JsonIgnore
    @ManyToMany(mappedBy = "programmingQuestions")
    private List<Exam> exams = new ArrayList<>(); 

    public void removeExam(Exam exam) {
        this.exams.remove(exam);
    }

    public ProgrammingQuestion(String questionText, List<TestCase> testCases, String difficulty_level) {
        this.questionText = questionText;
        this.testCases = testCases;
        this.difficulty_level = difficulty_level;
    }
}

