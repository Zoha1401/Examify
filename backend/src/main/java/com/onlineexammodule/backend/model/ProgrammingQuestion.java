package com.onlineexammodule.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "programmingQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCase> testCases = new ArrayList<>();

    private String difficulty;
    private String referenceAnswer;

    @JsonIgnore
    @ManyToMany(mappedBy = "programmingQuestions")
    private List<Exam> exams = new ArrayList<>();

    public void removeExam(Exam exam) {
        this.exams.remove(exam);
    }

    public ProgrammingQuestion(String programmingQuestionText, List<TestCase> testCases, String difficulty) {
        this.programmingQuestionText = programmingQuestionText;
        this.testCases = testCases != null ? testCases : new ArrayList<>();
        this.difficulty = difficulty;
    }
}

// Create an examiner
// With three exams diff questions
// Create a fourth exam with mixture of questions from above exam.

//Tomorrow: Finish edit vala part of questions
//By day after tomorrow: Do that create exam with questions, and select based on category. And also rectify the time format stuff
//Start with examinee by thursday