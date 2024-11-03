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
    @JoinColumn(name="examinerId")
    private Examiner examiner;

}