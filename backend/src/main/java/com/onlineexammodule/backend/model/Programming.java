package com.onlineexammodule.backend.model;

// import java.util.HashSet;
import java.util.List;
// import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Programming {
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    private String questionText;
    private String testCases;
    private String difficulty_level;
    private String reference_answer;

    @ManyToMany(mappedBy = "programmingQuestions")
    private List<Exam> exams; // Should be a collection

}
