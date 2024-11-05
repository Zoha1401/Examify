package com.onlineexammodule.backend.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Examinee {
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examineeId;

    private String email;
    private String college;
    private String degree;

    @ManyToOne
    @JoinColumn(name="examiner_id")
    @JsonBackReference //Breaks circular reference
    private Examiner examiner;

    @ManyToMany
    @JoinTable(
        name = "examinee_exam",
        joinColumns = @JoinColumn(name = "examinee_id"),
        inverseJoinColumns = @JoinColumn(name = "exam_id")
    )
    private List<Exam> exams;

    @OneToMany(mappedBy = "examinee")
    private List<Result> testResults;

}
