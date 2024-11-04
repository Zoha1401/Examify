package com.onlineexammodule.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
   
   public Examiner(String email) {
      this.email = email;
   }

}
