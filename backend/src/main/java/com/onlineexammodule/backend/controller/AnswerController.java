package com.onlineexammodule.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineexammodule.backend.DTO.McqAnswerDTO;
import com.onlineexammodule.backend.DTO.ProgrammingAnswerDTO;
import com.onlineexammodule.backend.model.Answer;
import com.onlineexammodule.backend.service.AnswerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/answer")
@CrossOrigin(origins = "http://localhost:3000")
public class AnswerController {
    
    @Autowired
    private AnswerService answerService;

    @GetMapping("/getExamSpecificAnswer")
    public Answer getExamSpecificAnswer(@RequestParam Long examineeId, @RequestParam Long examId) {
        return answerService.getExamSpecificAnswers(examineeId, examId);
    }

    @GetMapping("/getMcqAnswerDetail")
    public List<McqAnswerDTO> getMcqAnswerDetail(@RequestParam Long answerId) {
        return answerService.getMcqAnswersWithDetails(answerId);
    }

    @GetMapping("/getProgrammingAnswerDetail")
    public List<ProgrammingAnswerDTO> getProgrammingAnswerDetail(@RequestParam Long answerId) {
        return answerService.getProgrammingAnswerDetail(answerId);
    }
    
    
    
}
