package com.onlineexammodule.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.model.Examiner;
import com.onlineexammodule.backend.repo.ExamineeRepository;
// import com.onlineexammodule.backend.model.ExaminerPrincipal;
import com.onlineexammodule.backend.repo.ExaminerRepo;

@Service
public class ExaminerService {
    
   
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

    public Examinee addExaminee(Examinee examinee, String email) {
        
        Examiner examiner = examinerRepository.findByEmail(email);
        if(examiner==null)
        {
            throw new RuntimeException("Examinee with this email already exists");
        }

        Examinee new_examinee=examineeRepository.findByEmail(examinee.getEmail());
        if(new_examinee!=null)
        {
            throw new RuntimeException("Examinee with this email already exists");
        }

        examinee.setExaminer(examiner);
        return examineeRepository.save(examinee);
    }
}
