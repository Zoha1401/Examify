package com.onlineexammodule.backend.model;


import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
    private Integer year;
    private Integer phoneNumber;

    @Column
    private String password;

    //Hashing the phonw number for examinee side authentication and security
    public void setPhoneHash(String phoneNumber) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(phoneNumber);
    }

    public boolean isPhoneNumberMatch(String phoneNumber) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(phoneNumber, this.password);
    }

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
    @JsonManagedReference("examinee")
    private List<Answer> answers = new ArrayList<>();

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
