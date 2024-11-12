package com.onlineexammodule.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlineexammodule.backend.DTO.MCQ;
import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.McqQuestion;
import com.onlineexammodule.backend.model.QuestionOption;
import com.onlineexammodule.backend.repo.ExamRepository;
import com.onlineexammodule.backend.repo.McqRepository;
import com.onlineexammodule.backend.repo.QuestionOptionRepository;

@Service
public class McqService {

    private final ExamRepository examRepository;
    private final McqRepository mcqRepository;
    private final QuestionOptionRepository questionOptionRepository;

    public McqService(ExamRepository examRepository, McqRepository mcqRepository, QuestionOptionRepository questionOptionRepository)
    {
        this.examRepository=examRepository;
        this.mcqRepository=mcqRepository;
        this.questionOptionRepository=questionOptionRepository;
    }

    @Transactional
    public MCQ addMcqQuestion(McqQuestion mcqQuestion, Long examId) {
       
        if(!examRepository.existsById(examId))
        {
            throw new IllegalArgumentException("Exam not found");
        }

        System.out.println(mcqQuestion);  // Check if options list is populated as expected

        Exam exam=examRepository.getReferenceById(examId);
        List<QuestionOption> getQuestionOptions=mcqQuestion.getOptions();
        System.out.println("Number of options: " + getQuestionOptions.size());
        if(getQuestionOptions!=null)
        {
            List<QuestionOption> updatedOptions = new ArrayList<>(); 
            System.out.println("Inside options");
            for(QuestionOption option_temp: getQuestionOptions)
            {
                if(option_temp.isCorrect()!=true)
                {
                    option_temp.setCorrect(false);
                    
                }
                option_temp.addMcqQuestion(mcqQuestion);
                updatedOptions.add(option_temp);
                if(option_temp.getOptionId()==null){
                questionOptionRepository.save(option_temp);
                System.out.println("Saved option");
                }
                    
            }
            mcqQuestion.setOptions(updatedOptions);
        }
        System.out.println("MCQ is added to exam with Id "+examId);
        exam.getMcqQuestions().add(mcqQuestion);
        
        McqQuestion new_mcqQuestion=mcqRepository.save(mcqQuestion);
        
        return convertToDto(new_mcqQuestion);
    }
    

    //Update MCQ
@Transactional
public MCQ updateMcqQuestion(Long mcqId, McqQuestion updatedMcqQuestion, Long examId) {
   
    if (!examRepository.existsById(examId)) {
        throw new IllegalArgumentException("Exam not found");
    }

   
    McqQuestion existingMcqQuestion = mcqRepository.findById(mcqId)
            .orElseThrow(() -> new IllegalArgumentException("MCQ not found"));

    // Update MCQ properties
    existingMcqQuestion.setQuestionText(updatedMcqQuestion.getQuestionText());
    existingMcqQuestion.setCategory(updatedMcqQuestion.getCategory());
    existingMcqQuestion.setCorrectAnswer(updatedMcqQuestion.getCorrectAnswer());

  
    List<QuestionOption> updatedOptions = new ArrayList<>();
    for (QuestionOption option : updatedMcqQuestion.getOptions()) {
        if (option.getOptionId() != null) {

            QuestionOption existingOption = questionOptionRepository.findById(option.getOptionId())
                    .orElseThrow(() -> new IllegalArgumentException("Option not found"));
            existingOption.setOptionText(option.getOptionText());
            existingOption.setCorrect(option.isCorrect());
            updatedOptions.add(existingOption);
        } else {
            option.addMcqQuestion(existingMcqQuestion);
            questionOptionRepository.save(option);
            updatedOptions.add(option);
        }
    }
    existingMcqQuestion.setOptions(updatedOptions);

    mcqRepository.save(existingMcqQuestion);
    return convertToDto(existingMcqQuestion);
}

    //Delete MCQ


    private MCQ convertToDto(McqQuestion mcqQuestion) {
        MCQ mcqDto = new MCQ();
        mcqDto.setMcqId(mcqQuestion.getMcqId());
        mcqDto.setCategory(mcqQuestion.getCategory());
        mcqDto.setCorrectAnswer(mcqQuestion.getCorrectAnswer());
        mcqDto.setQuestionText(mcqQuestion.getQuestionText());
        return mcqDto;
    }

    public String deleteMcqQuestion(Long mcqId, Long examId) {
        
        Exam exam = examRepository.findById(examId)
        .orElseThrow(() -> new IllegalArgumentException("Exam not found"));


        McqQuestion mcqQuestion = mcqRepository.findById(mcqId)
        .orElseThrow(() -> new IllegalArgumentException("MCQ not found"));

        exam.getMcqQuestions().remove(mcqQuestion);
        List<QuestionOption> optionsToRemove = mcqQuestion.getOptions();
       
        if(optionsToRemove!=null){
        for(QuestionOption option:optionsToRemove)
        {
            option.getMcqQuestions().remove(mcqQuestion);
        }
    }
        mcqRepository.delete(mcqQuestion);

        return "Deleted";
    }
}
