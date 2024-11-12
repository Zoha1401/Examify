package com.onlineexammodule.backend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.McqQuestion;
import com.onlineexammodule.backend.repo.ExamRepository;
import com.onlineexammodule.backend.repo.McqRepository;

@Service
public class McqService {

    private final ExamRepository examRepository;
    private final McqRepository mcqRepository;

    public McqService(ExamRepository examRepository, McqRepository mcqRepository)
    {
        this.examRepository=examRepository;
        this.mcqRepository=mcqRepository;
    }
    public McqQuestion addMcqQuestion(McqQuestion mcqQuestion, Long examId) {
       

        if(!examRepository.existsById(examId))
        {
            throw new IllegalArgumentException("Exam not found");
        }
        Exam exam=examRepository.getReferenceById(examId);

        exam.getMcqQuestions().add(mcqQuestion);

        McqQuestion new_mcqQuestion=mcqRepository.save(mcqQuestion);

        return new_mcqQuestion;
    }
    
}
