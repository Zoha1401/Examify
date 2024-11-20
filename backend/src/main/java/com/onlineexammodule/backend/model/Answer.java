package com.onlineexammodule.backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    private LocalDateTime submitDateTime;
    private boolean passed;
    private boolean submitted;
    private int mcqScore;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "examinee_id", nullable = false)
    private Examinee examinee;
    
    
    @OneToMany(mappedBy = "answer", orphanRemoval = true)
    private List<McqAnswer> mcqAnswers = new ArrayList<>();

    @OneToMany(mappedBy = "answer", orphanRemoval = true)
    private List<ProgrammingQuestionAnswer> programmingQuestionAnswers = new ArrayList<>();
}
