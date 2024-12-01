package com.onlineexammodule.backend.service;


import java.util.List;


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

import com.onlineexammodule.backend.model.Exam;
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
        String email=examiner.getEmail();

        Examiner existingExaminer=examinerRepository.findByEmail(email);

        if(existingExaminer!=null)
        {
            throw new IllegalArgumentException("Examiner already exists"); 
        }
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
        
        //Get examiner through email extracted from token
        Examiner examiner = examinerRepository.findByEmail(email);
       
        if(examiner==null)
        {
            throw new RuntimeException("Examiner does not exist");
        }
        System.out.println("Inside Service examiner id "+examiner.getExaminerId());
       
        //Check if examinee is saved
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


    public Examinee deleteExaminee(Long examineeId, String examiner_email) {

          //Get examiner through email extracted from token
        Examiner examiner=examinerRepository.findByEmail(examiner_email);

        if (examiner == null) {
            throw new IllegalArgumentException("Examiner not found with email: " + examiner_email);
        }
        
        //Find examinee and delete
        Examinee examinee = examineeRepository.findById(examineeId)
        .orElseThrow(() -> new IllegalArgumentException("Examinee not found with ID: " + examineeId));

        examiner.removeExaminee(examinee);
        examinerRepository.save(examiner); 
        
        if (examinee.getExaminers().isEmpty()) {
            examineeRepository.delete(examinee);
            System.out.println("Deleted examinee from database as it has no other associated examiners.");
        } else {
            System.out.println("Examinee removed from examiner but not deleted as it is associated with other examiners.");
        }
    
        return examinee;




        
    }


    public Examinee updateExaminee(Examinee updatedExaminee, Long examineeId) {

        //Fetch examinee to be updated
        if (examineeId == null) {
            throw new IllegalArgumentException("Examinee ID is required for update.");
        }

        Examinee existingExaminee = examineeRepository.findById(examineeId)
            .orElseThrow(() -> new IllegalArgumentException("Examinee not found with ID: " + updatedExaminee.getExamineeId()));
        
        //Update values if set
        if(updatedExaminee.getCollege()!=null)
        existingExaminee.setCollege(updatedExaminee.getCollege());

        if(updatedExaminee.getEmail()!=null)
        existingExaminee.setEmail(updatedExaminee.getEmail());

        if(updatedExaminee.getDegree()!=null)
        existingExaminee.setDegree(updatedExaminee.getDegree());
    
        return examineeRepository.save(existingExaminee);
    }


    public Examinee getExaminee(String examinee_email, String examiner_email) {
        
        //Get examiner by email
        Examiner examiner=examinerRepository.findByEmail(examiner_email);

        if(examiner==null)
        {
            throw new IllegalArgumentException("Examiner not found with email "+ examiner_email);
        }
        
        //Get examinee from list of examiners.
        return examiner.getExaminees().stream()
               .filter(examinee->examinee.getEmail().equals(examinee_email))
               .findFirst()
               .orElseThrow(()-> new IllegalArgumentException("Examinee not found with email "+ examinee_email + " under examiner "+ examiner_email));


    }


    public List<Examinee> getAllExaminee(String examiner_email) {
        Examiner examiner=examinerRepository.findByEmail(examiner_email);

        if (examiner == null) {
            throw new IllegalArgumentException("Examiner not found with email: " + examiner_email);
        }

        return examiner.getExaminees();
    }


    public Examinee getExamineeById(Long examineeId) {
        Examinee fetchExaminee=examineeRepository.findById(examineeId)
                               .orElseThrow(()-> new IllegalArgumentException("Examinee not found"));
        
        return fetchExaminee;

    }


    public List<Exam> getAllExams(String examiner_email) {
        Examiner examiner=examinerRepository.findByEmail(examiner_email);
                          
        if (examiner == null) {
            throw new IllegalArgumentException("Examiner not found with email: " + examiner_email);
        }

        return examiner.getExams();
    }


}
