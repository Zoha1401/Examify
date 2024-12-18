package com.onlineexammodule.backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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

    private boolean passed;
    private boolean submitted;
    private int mcqScore;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    @JsonBackReference("exam")
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "examinee_id", nullable = false)
    @JsonBackReference("examinee")
    private Examinee examinee;
    
    
    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<McqAnswer> mcqAnswers = new ArrayList<>();

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProgrammingQuestionAnswer> programmingQuestionAnswers = new ArrayList<>();

    
}

