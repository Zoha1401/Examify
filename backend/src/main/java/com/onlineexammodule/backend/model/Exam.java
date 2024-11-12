package com.onlineexammodule.backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    private int McqpassingScore;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "examiner_id", nullable = false)
    private Examiner examiner;
    
   
    @ManyToMany
    @JsonIgnore
    @JoinTable(
        name = "programming_exam", // The join table
        joinColumns = @JoinColumn(name = "exam_id"), // Foreign key for Exam
        inverseJoinColumns = @JoinColumn(name = "programming_id") // Foreign key for Programming
    )
    private List<ProgrammingQuestion> programmingQuestions=new ArrayList<>();
    
   
    @ManyToMany
    @JsonIgnore
    @JoinTable(
    name = "exam_mcq",  // Join table
    joinColumns = @JoinColumn(name = "exam_id"),  // Foreign key for Exam
    inverseJoinColumns = @JoinColumn(name = "mcq_id")  // Foreign key for McqQuestion
)
private List<McqQuestion> mcqQuestions=new ArrayList<>();
   

    @ManyToMany(mappedBy = "exams")
    @JsonIgnore
    private List<Examinee> examinees=new ArrayList<>();

    @OneToMany(mappedBy = "exam")
    private List<Result> testResults=new ArrayList<>();

 
  public void addExaminee(Examinee examinee){
    if(!this.examinees.contains(examinee)){
        this.examinees.add(examinee);
        examinee.getExams().add(this);
    }
  }

  public void addMcqQuestion(McqQuestion question) {
    if (!this.mcqQuestions.contains(question)) {
        this.mcqQuestions.add(question);
        question.getExams().add(this); // If it's a many-to-one relationship
    }
}

public void addProgrammingQuestion(ProgrammingQuestion question) {
    if (!this.programmingQuestions.contains(question)) {
        this.programmingQuestions.add(question);
        question.getExams().add(this); // If it's a many-to-one relationship
    }
}



}
