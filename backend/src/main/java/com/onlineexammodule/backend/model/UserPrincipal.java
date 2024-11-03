package com.onlineexammodule.backend.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
    
    private Examiner examiner;

    public UserPrincipal(Examiner examiner){
        this.examiner=examiner;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        
        return examiner.getPassword();
        
    }

    @Override
    public String getUsername() {
       return examiner.getEmail();
       
    }

    @Override
    public boolean isAccountNonExpired() {
       
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
       
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        
        return true;
    }

    @Override
    public boolean isEnabled() {
        
        return true;
    }
}
