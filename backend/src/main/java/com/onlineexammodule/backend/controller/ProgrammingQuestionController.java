package com.onlineexammodule.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlineexammodule.backend.model.ProgrammingQuestion;
import com.onlineexammodule.backend.model.TestCase;
import com.onlineexammodule.backend.service.ProgrammingService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/programmingQuestion")
@CrossOrigin
public class ProgrammingQuestionController {

    @Autowired
    private ProgrammingService programmingService;

       //Add programming question
     @PostMapping("/addProgrammingQuestion")
     public ProgrammingQuestion addProgrammingQuestion(@RequestBody ProgrammingQuestion ProgrammingQuestion, @RequestParam Long examId) {
        ProgrammingQuestion new_prog_ques=programmingService.addProgrammingQuestion(ProgrammingQuestion, examId);
        return new_prog_ques;
     }

     @GetMapping("/fetchTestCases")
     public List<TestCase> fetchTestCase(@RequestParam Long pro_id) {
         return programmingService.fetchTestCases(pro_id);
     }

     @PostMapping("/updateProgrammingQuestion")
     public ProgrammingQuestion updateProgrammingQuestion(@RequestBody ProgrammingQuestion ProgrammingQuestion, @RequestParam Long examId) {
        ProgrammingQuestion up_prog_ques=programmingService.updateProgrammingQuestion(ProgrammingQuestion, examId);
        return up_prog_ques;
     }

     @DeleteMapping("/deleteProgrammingQuestion")
     public ProgrammingQuestion deleteProgrammingQuestion(@RequestBody ProgrammingQuestion ProgrammingQuestion, @RequestParam Long examId) {
        ProgrammingQuestion up_prog_ques=programmingService.deleteProgrammingQuestion(ProgrammingQuestion, examId);
        return up_prog_ques;
     }
     

     
}
