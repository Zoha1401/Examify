package com.onlineexammodule.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.onlineexammodule.backend.model.ProgrammingQuestion;
import com.onlineexammodule.backend.service.ProgrammingService;

public class ProgrammingQuestionController {

    @Autowired
    private ProgrammingService programmingService;

       //Add programming question
     @PostMapping("/addProgrammingQuestion")
     public ProgrammingQuestion addProgrammingQuestion(@RequestBody ProgrammingQuestion ProgrammingQuestion, @RequestParam Long examId) {
        ProgrammingQuestion new_prog_ques=programmingService.addProgrammingQuestion(ProgrammingQuestion, examId);
        return new_prog_ques;
     }
}
