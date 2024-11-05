package com.onlineexammodule.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private Integer mcqScore;
    private String codeSubmissions;

    @ManyToOne
    @JoinColumn(name = "examinee_id")
    private Examinee examinee;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;
}
