package com.onlineexammodule.backend.model;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
// import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"examiners"})
public class Examinee {
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examineeId;

    private String email;
    private String college;
    private String degree;

    @ManyToMany(mappedBy = "examinees")
    @JsonBackReference
    private List<Examiner> examiners = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    @JoinTable(
        name = "examinee_exam",
        joinColumns = @JoinColumn(name = "examinee_id"),
        inverseJoinColumns = @JoinColumn(name = "exam_id")
    )
    private List<Exam> exams= new ArrayList<>();

    @OneToMany(mappedBy = "examinee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "examinee")
    private List<Result> testResults= new ArrayList<>();

    public Examinee(String email, Examiner examiner) {
        this.email = email;
        
     }
    
     public void removeExam(Exam exam){
        if(this.getExams().contains(exam))
        {
            this.getExams().remove(exam);
        }
     }

}
