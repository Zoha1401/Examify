package com.onlineexammodule.backend.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ExamineePrincipal implements UserDetails{


    private Examinee examinee;

    public ExamineePrincipal(Examinee examinee){
        this.examinee=examinee;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return null;
    }
   
    @Override
    public String getUsername() {
       return examinee.getEmail();
    }

}
