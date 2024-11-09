package com.onlineexammodule.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.onlineexammodule.backend.DTO.ExamineeLoginRequest;
import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.model.Examiner;
// import com.onlineexammodule.backend.model.Examiner;
import com.onlineexammodule.backend.repo.ExamineeRepository;
// import org.springframework.security.core.AuthenticationException;



@Service
public class ExamineeService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ExamineeRepository examineeRepository;

    public String verify(String email, Long examinerId)
    {
        Examinee examinee=examineeRepository.findByEmail(email);
        System.out.println("Finding examinee by email"+email);
        if (examinee == null) {
            System.out.println("Examinee not found for the provided email and examiner ID.");
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(email);
        
       
    }

}
