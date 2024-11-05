package com.onlineexammodule.backend.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Exam {
       
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int duration;

    @ManyToOne
    @JoinColumn(name = "examiner_id", nullable = false)
    private Examiner examiner;

    @OneToMany(mappedBy = "exam")
    private List<Programming> programmingQuestions;

    @OneToMany(mappedBy = "exam")
    private List<Mcq> mcqQuestions;

    @ManyToMany(mappedBy = "exams")
    private List<Examinee> examinees;

    @OneToMany(mappedBy = "exam")
    private List<Result> testResults;
}
