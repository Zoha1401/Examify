package com.onlineexammodule.backend.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.model.Examiner;
import com.onlineexammodule.backend.repo.ExamineeRepository;
// import com.onlineexammodule.backend.model.ExaminerPrincipal;
import com.onlineexammodule.backend.repo.ExaminerRepo;

@Service
public class ExaminerService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExaminerService.class);
    @Autowired
    private final ExamineeRepository examineeRepository;
    
    @Autowired
    private final ExaminerRepo examinerRepository;
    public ExaminerService(ExaminerRepo examinerRepository, ExamineeRepository examineeRepository) {
        this.examinerRepository = examinerRepository;
        this.examineeRepository=examineeRepository;
     
    }



    @Autowired
    private AuthenticationManager authManager;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

   

    public Examiner signInExaminer(Examiner examiner) {
        System.out.println(examiner);
        return examinerRepository.save(examiner);
    }
    

    public String verify(Examiner examiner) {
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(examiner.getEmail(), examiner.getPassword()));
                String token=jwtService.generateToken(examiner.getEmail());
            return token;
        } catch (AuthenticationException ex) {
            System.out.println(ex.getLocalizedMessage());
            return "Fail";
        }
    }
 
    @Transactional
    public Examinee addExaminee(Examinee examinee, String email) {
        
        Examiner examiner = examinerRepository.findByEmail(email);
       
        if(examiner==null)
        {
            throw new RuntimeException("Examiner does not exist");
        }
        System.out.println("Inside Service examiner id "+examiner.getExaminerId());
       

        Examinee existExaminee=examineeRepository.findByEmail(examinee.getEmail());
        
         Examinee examineeToSave;
        if (existExaminee!=null) {
            // Use existing examinee
            examineeToSave = existExaminee;
            logger.info("Examinee already exists with ID: {}", examineeToSave.getExamineeId());
        } else {
            // New examinee to add
           
            examineeToSave = examinee;
            logger.info("Creating new examinee with details: {}", examineeToSave);
        }
    
        // Add examiner to examinee's list of examiners if not already present
        if (!examineeToSave.getExaminers().contains(examiner)) {
            examineeToSave.getExaminers().add(examiner);
        }
        else {
            logger.warn("Examiner with ID {} is already associated with Examinee ID {}", examiner.getExaminerId(), examineeToSave.getEmail());
        }
    
        // Add examinee to examiner's list if not already present
        if (!examiner.getExaminees().contains(examineeToSave)) {
            examiner.getExaminees().add(examineeToSave);
        }
    
        Examinee savedExaminee = examineeRepository.save(examineeToSave);
    
        System.out.println("Inside Service size of examinee's examiners " + savedExaminee.getExaminers().size());
        return savedExaminee;
        
    }


    public void deleteExaminee(String email, String examiner_email) {
        Examinee examinee = examineeRepository.findByEmail(email);

        Examiner examiner=examinerRepository.findByEmail(examiner_email);

       

        if(examiner!=null)
        {
            examiner.removeExaminee(examinee);
            examinerRepository.save(examiner);
        }
        
        if(examinee!=null)
        {
            examineeRepository.delete(examinee);
        }

        
    }


    public Examinee updateExaminee(Examinee updatedExaminee) {
        Examinee existingExaminee = examineeRepository.findByEmail(updatedExaminee.getEmail());

    // Update fields
    existingExaminee.setCollege(updatedExaminee.getCollege());
    existingExaminee.setEmail(updatedExaminee.getEmail());
    existingExaminee.setDegree(updatedExaminee.getDegree());
    // Add other fields to update as needed

    // Save the updated entity
    return examineeRepository.save(existingExaminee);
    }
}
