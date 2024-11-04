package com.onlineexammodule.backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.onlineexammodule.backend.model.Examiner;
import com.onlineexammodule.backend.model.ExaminerPrincipal;
import com.onlineexammodule.backend.repo.ExaminerRepo;

@Service
public class ExaminerDetailService implements UserDetailsService{

    private final ExaminerRepo repo;
    public ExaminerDetailService(ExaminerRepo repo) {
        this.repo = repo;
     
    }

   

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Attempting to load user: " + email);
        Examiner examiner = repo.findByEmail(email);
        if (examiner == null) {
            System.out.println("Examiner not found");
            throw new UsernameNotFoundException("Examiner not found");
        }

        System.out.println("Loaded user: " + examiner.getEmail());
        return new ExaminerPrincipal(examiner);
    }
}
