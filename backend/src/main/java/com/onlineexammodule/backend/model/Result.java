package com.onlineexammodule.backend.model;

import java.time.LocalDateTime;
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

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Result {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;
    private LocalDateTime submitDateTime;
    private boolean passed;
    private boolean submitted;
    private int mcqScore;

    @ManyToOne
    @JoinColumn(name = "examinee_id")
    private Examinee examinee;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;


    @OneToMany(mappedBy = "examResult")
    private List<Answer> answers;


}
