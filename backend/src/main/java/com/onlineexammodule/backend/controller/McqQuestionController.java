package com.onlineexammodule.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlineexammodule.backend.model.McqQuestion;
import com.onlineexammodule.backend.service.McqService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/mcq")
@CrossOrigin
public class McqQuestionController {
    
     
    @Autowired
    private McqService mcqService;

    @PostMapping("/addMcqQuestion")
     public McqQuestion addMcqQuestion(@RequestBody McqQuestion McqQuestion, @RequestParam Long examId) {
        McqQuestion new_mcq=mcqService.addMcqQuestion(McqQuestion, examId);
        return new_mcq;
     }

    
}
