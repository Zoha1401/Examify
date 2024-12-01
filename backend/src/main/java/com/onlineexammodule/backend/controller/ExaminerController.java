package com.onlineexammodule.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.model.Examiner;


import com.onlineexammodule.backend.service.ExaminerService;
import com.onlineexammodule.backend.service.JWTService;


import jakarta.servlet.http.HttpServletRequest;

//import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/examiner")
@CrossOrigin(origins = "http://localhost:3000")
public class ExaminerController {

    @Autowired
    private ExaminerService examinerService;
    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);  //Can specify strength
    
    @Autowired
    private JWTService jwtService;

  
    @PostMapping("/signin")
    public ResponseEntity<?> signInExaminer(@RequestBody Examiner examiner) {

        try {
        examiner.setPassword(encoder.encode(examiner.getPassword()));
        Examiner newExaminer = examinerService.signInExaminer(examiner);
        return new ResponseEntity<>(newExaminer, HttpStatus.CREATED);
       
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    //API endpoint for examiner login
     @PostMapping("/login")
     public ResponseEntity<String> login(@RequestBody Examiner examiner) {
        System.out.println(examiner.getExaminees().size());
        String token=examinerService.verify(examiner);
        return ResponseEntity.ok(token);
     }

    
     //API endpoint for testing authentication
     @GetMapping("/getSomething")
     public String greet() {
         return "JWT token validation done";
     }

     //API endpoint to add the Examinee
     @PostMapping("/addExaminee")
     public ResponseEntity<Examinee> addExaminee(@RequestBody Examinee examinee, HttpServletRequest request) {
         String token=request.getHeader("Authorization").substring(7);

         String email=jwtService.extractEmail(token);
         System.out.println("Examiner Email "+email);
         System.out.println("Inisde Controller "+ examinee);
         Examinee savedExaminee= examinerService.addExaminee(examinee, email);
         return new ResponseEntity<>(savedExaminee, HttpStatus.CREATED);
     }

     
     //API endpoint to delete the Examinee
     @DeleteMapping("/deleteExaminee")
     public ResponseEntity<Examinee> deleteExaminee(@RequestParam Long examineeId,  HttpServletRequest request) {
        String token=request.getHeader("Authorization").substring(7);
        String examiner_email=jwtService.extractEmail(token);
        Examinee deletedExaminee=examinerService.deleteExaminee(examineeId, examiner_email);
        if (deletedExaminee != null) {
            return ResponseEntity.ok(deletedExaminee);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
     }
     
     
     //API endpoint to update the examinee
     @PostMapping("/updateExaminee")
     public ResponseEntity<Examinee> updateExaminee(@RequestBody Examinee examinee, @RequestParam Long examineeId) {
       Examinee updatedExaminee=examinerService.updateExaminee(examinee, examineeId);
       return new ResponseEntity<>(updatedExaminee, HttpStatus.OK);
     }

     // Fetch examinee by Email
     @GetMapping("/getExamineeByEmail")
     public Examinee getExaminee(@RequestBody String examinee_email, HttpServletRequest request) {
         String token=request.getHeader("Authorization").substring(7);
         String examiner_email=jwtService.extractEmail(token);
         System.out.println("Examinee email "+examinee_email);
         System.out.println("Examiner Email "+examiner_email);
         Examinee examinee=examinerService.getExaminee(examinee_email, examiner_email);
         return examinee;
     }

     // Fetch all examinees
     @GetMapping("/getAllExaminee")
     public List<Examinee> getAllExaminee(HttpServletRequest request) {
         String token=request.getHeader("Authorization").substring(7);
         String examiner_email=jwtService.extractEmail(token);
         List<Examinee> examinees=examinerService.getAllExaminee(examiner_email);

         return examinees;
     }
     
     @GetMapping("/getExamineeById")
     public Examinee getExamineeById(@RequestParam Long examineeId) {
         return examinerService.getExamineeById(examineeId);
     }
     

    @GetMapping("/getAllExams")
    public List<Exam> getAllExams(HttpServletRequest request) {
        String token=request.getHeader("Authorization").substring(7);
        String examiner_email=jwtService.extractEmail(token);
        return examinerService.getAllExams(examiner_email);
    }
    
     
     
     
     
     
     
    
    


    // @PostMapping("/signin")
    // public ResponseEntity<?> signInExaminer(@RequestBody Examiner examiner) {
    //     try {
    //         Examiner newExaminer = service.signInExaminer(examiner);
    //         return new ResponseEntity<>(newExaminer, HttpStatus.CREATED);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }
}
