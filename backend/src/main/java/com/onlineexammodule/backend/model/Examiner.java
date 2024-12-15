package com.onlineexammodule.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

// import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"examinees"}) 
public class Examiner {
     
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long examinerId;
   private String email;
   private String password;
   
   @JsonManagedReference
   @OneToMany(mappedBy = "examiner")
   private List<Exam> exams= new ArrayList<>();;


  @ManyToMany
  @JoinTable(
    name = "examiner_examinee",  // Link table name
    joinColumns = @JoinColumn(name = "examiner_id"),
    inverseJoinColumns = @JoinColumn(name = "examinee_id")
  )
  @JsonManagedReference
  private List<Examinee> examinees = new ArrayList<>();
   
   public Examiner(String email) {
      this.email = email;
   }

   public void removeExaminee(Examinee examinee) {
      examinees.remove(examinee);
   }
}
