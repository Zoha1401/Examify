package com.onlineexammodule.backend.model;

import java.util.List;

// import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Examiner {
     
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long examinerId;
   private String email;
   private String password;

   @OneToMany(mappedBy = "examiner")
   private List<Exam> exams;


   @OneToMany(mappedBy = "examiner")
   // @JsonManagedReference // Indicates the parent side of the relationship
   private List<Examinee> examinees;
   
   public Examiner(String email) {
      this.email = email;
   }

}
