package com.onlineexammodule.backend.service;

// import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
    
    private String secretKey="";

    public JWTService(){

        try{
        KeyGenerator keyGen=KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk=keyGen.generateKey();
        secretKey=Base64.getEncoder().encodeToString(sk.getEncoded());
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }
    public String generateToken(String email){
        
        Map<String, Object> claims=new HashMap<>();
        return Jwts.builder()
               .claims()
               .add(claims)
               .subject(email)
               .issuedAt(new Date(System.currentTimeMillis()))
               .expiration(new Date(System.currentTimeMillis()+60*60*30*1000L))
               .and()
               .signWith(getKey())
               .compact();

    }


    public String generateExamineeToken(String email){
        
        Map<String, Object> claims=new HashMap<>();
        return Jwts.builder()
               .claims()
               .add(claims)
               .subject(email)
               .issuedAt(new Date(System.currentTimeMillis()))
               .expiration(new Date(System.currentTimeMillis()+60*60*30*1000L))
               .and()
               .signWith(getKey())
               .compact();

    }


    private SecretKey getKey() {
        byte [] keyBytes=Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String extractEmail(String token) {

        // extract email from jwt token
        System.out.println("Extracting email");
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        System.out.println("Validating token for user: " + userDetails.getUsername());
        final String email=extractEmail(token);
        System.out.println(email);
        System.out.println(userDetails.getUsername());
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
