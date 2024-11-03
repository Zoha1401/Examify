package com.onlineexammodule.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.onlineexammodule.backend.model.Examiner;
import com.onlineexammodule.backend.model.UserPrincipal;
import com.onlineexammodule.backend.repo.ExaminerRepo;
import java.util.Optional;

@Service
public class ExaminerService implements UserDetailsService {

    @Autowired
    private ExaminerRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Examiner examiner = repo.findByEmail(email);

        if (examiner==null) {
            System.out.println("Examiner not found");
            throw new UsernameNotFoundException("Examiner not found");
        }

      
        return new UserPrincipal(examiner);
    }

    
}
