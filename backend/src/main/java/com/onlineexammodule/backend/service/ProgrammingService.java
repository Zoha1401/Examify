package com.onlineexammodule.backend.service;

import org.springframework.stereotype.Service;

import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.ProgrammingQuestion;
import com.onlineexammodule.backend.repo.ExamRepository;
import com.onlineexammodule.backend.repo.ProgrammingRepository;

@Service
public class ProgrammingService {

    private final ProgrammingRepository programmingRepository;
    private final ExamRepository examRepository;

    public ProgrammingService(ProgrammingRepository programmingRepository, ExamRepository examRepository)
    {
        this.programmingRepository=programmingRepository;
        this.examRepository=examRepository;
    }
    public ProgrammingQuestion addProgrammingQuestion(ProgrammingQuestion programmingQuestion, Long examId) {
           if(!programmingRepository.existsById(examId))
        {
            throw new IllegalArgumentException("Exam not found");
        }
        Exam exam=examRepository.getReferenceById(examId);

        exam.getProgrammingQuestions().add(programmingQuestion);

        ProgrammingQuestion new_mcqQuestion=programmingRepository.save(programmingQuestion);

        return new_mcqQuestion;
    }

 // Update pro

 //Delete Pro




 //Exam Management
 //Create*, update, Delete*

 //Question Management
 //Add, Update Delete MCQ, 
 //Add Update Delete Programming

 //MCQ management
 //Update, Add, delete Options


 //----> Next week ----> Submit Exam -> Examinee submit answers
//              ----> Show Exam Result.

//Integration with frontend


    
}
