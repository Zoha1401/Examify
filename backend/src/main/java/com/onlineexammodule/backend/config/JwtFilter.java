package com.onlineexammodule.backend.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.onlineexammodule.backend.service.ExaminerDetailService;
import com.onlineexammodule.backend.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{
    
    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        // Bearer token-> client side
        String authHeader=request.getHeader("Authorization");
        String token=null;
        String email=null;

        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);
            email=jwtService.extractEmail(token);
        }

        if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            UserDetails userDetails=context.getBean(ExaminerDetailService.class).loadUserByUsername(email);
            System.out.println(jwtService.validateToken(token, userDetails));
            if(jwtService.validateToken(token, userDetails)){
               UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
               System.out.println(authToken);
               authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    
        filterChain.doFilter(request, response);
           
    
}
}
