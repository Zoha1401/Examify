package com.onlineexammodule.backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.model.ExamineePrincipal;

import com.onlineexammodule.backend.repo.ExamineeRepository;


@Service
public class ExamineeDetailService implements UserDetailsService{
    
    private final ExamineeRepository examineeRepository;
    public ExamineeDetailService(ExamineeRepository examineeRepository) {
        this.examineeRepository = examineeRepository;
     
    }




    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Attempting to load user: " + email);
        Examinee examinee= examineeRepository.findByEmail(email);
        if (examinee == null) {
            System.out.println("Examinee not found");
            throw new UsernameNotFoundException("Examinee not found");
        }

        System.out.println("Loaded user: " + examinee.getEmail());
        return new ExamineePrincipal(examinee);
    }
}
