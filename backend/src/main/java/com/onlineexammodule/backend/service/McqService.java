package com.onlineexammodule.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public McqService(ExamRepository examRepository, McqRepository mcqRepository,
            QuestionOptionRepository questionOptionRepository) {
        this.examRepository = examRepository;
        this.mcqRepository = mcqRepository;
        this.questionOptionRepository = questionOptionRepository;
    }

    @Transactional
    public MCQ addMcqQuestion(McqQuestion mcqQuestion, Long examId) {
        //Check if exam exists
        if (!examRepository.existsById(examId)) {
            throw new IllegalArgumentException("Exam not found");
        }
        
        //GET exam if exists
        Exam exam = examRepository.getReferenceById(examId);

        List<QuestionOption> getQuestionOptions = mcqQuestion.getOptions();
        System.out.println("Number of options: " + (getQuestionOptions != null ? getQuestionOptions.size() : 0));
        
        //Get options and save them based on correct and incorrect
        if (getQuestionOptions != null && !getQuestionOptions.isEmpty()) {
            List<QuestionOption> updatedOptions = new ArrayList<>();

            for (QuestionOption option : getQuestionOptions) {
                if (option.isCorrect() != true) {
                    option.setCorrect(false);
                }
                option.setMcqQuestion(mcqQuestion); 
                questionOptionRepository.save(option); 
                updatedOptions.add(option);
            }

            mcqQuestion.setOptions(updatedOptions); // Set updated options on mcqQuestion
        }

       
        exam.getMcqQuestions().add(mcqQuestion);
        mcqRepository.save(mcqQuestion); 
        examRepository.save(exam); 

      
        return convertToDto(mcqQuestion);
    }

    // Update MCQ
    @Transactional
    public MCQ updateMcqQuestion(Long mcqId, McqQuestion updatedMcqQuestion, Long examId) {

         //Check if exam exists
        if (!examRepository.existsById(examId)) {
            throw new IllegalArgumentException("Exam not found");
        }

        //Fetch existing mcq
        McqQuestion existingMcqQuestion = mcqRepository.findById(mcqId)
                .orElseThrow(() -> new IllegalArgumentException("MCQ not found"));

        //Set values
        existingMcqQuestion.setQuestionText(updatedMcqQuestion.getQuestionText());
        existingMcqQuestion.setCategory(updatedMcqQuestion.getCategory());
        existingMcqQuestion.setCorrectAnswer(updatedMcqQuestion.getCorrectAnswer());

        //Set options
        List<QuestionOption> updatedOptions = new ArrayList<>();
        for (QuestionOption option : updatedMcqQuestion.getOptions()) {
            if (option.getOptionId() != null) {
                
                QuestionOption existingOption = questionOptionRepository.findById(option.getOptionId())
                        .orElseThrow(() -> new IllegalArgumentException("Option not found"));
                existingOption.setOptionText(option.getOptionText());
                existingOption.setCorrect(option.isCorrect());
                updatedOptions.add(existingOption); 
            } else {
                
                option.setMcqQuestion(existingMcqQuestion); 
                questionOptionRepository.save(option); 
                updatedOptions.add(option);
            }
        }

        existingMcqQuestion.setOptions(updatedOptions);
        mcqRepository.save(existingMcqQuestion);
        return convertToDto(existingMcqQuestion);

    }

    
    @Transactional
    private MCQ convertToDto(McqQuestion mcqQuestion) {
        MCQ mcqDto = new MCQ();
        mcqDto.setMcqId(mcqQuestion.getMcqId());
        mcqDto.setCategory(mcqQuestion.getCategory());
        mcqDto.setCorrectAnswer(mcqQuestion.getCorrectAnswer());
        mcqDto.setQuestionText(mcqQuestion.getQuestionText());
        return mcqDto;
    }
    
    // Delete MCQ
    @Transactional
    public String deleteMcqQuestion(Long mcqId, Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        McqQuestion mcqQuestion = mcqRepository.findById(mcqId)
                .orElseThrow(() -> new IllegalArgumentException("MCQ not found"));
        
        //Remove mcq question from exam
        exam.getMcqQuestions().remove(mcqQuestion);
        examRepository.save(exam);
        
        //Remove options from Option
        List<QuestionOption> optionsToRemove = mcqQuestion.getOptions();

        if (optionsToRemove != null) {
            for (QuestionOption option : optionsToRemove) {
                option.setMcqQuestion(null);
                questionOptionRepository.save(option);
            }
        }
        //
        mcqQuestion.setOptions(new ArrayList<>());
        mcqRepository.save(mcqQuestion);

        mcqRepository.delete(mcqQuestion);

        return "Deleted";

    }

    public ResponseEntity<QuestionOption> addOption(Long mcqId, QuestionOption questionOption) {
        // Find the MCQ to be updated
        McqQuestion mcqQuestion = mcqRepository.findById(mcqId)
                .orElseThrow(() -> new IllegalArgumentException("Mcq question not found"));

        // Create a new option
        mcqQuestion.getOptions().add(questionOption);
        questionOption.setMcqQuestion(mcqQuestion);
        questionOptionRepository.save(questionOption);
        mcqRepository.save(mcqQuestion);

        return new ResponseEntity<>(questionOption, HttpStatus.OK);
    }

    public ResponseEntity<QuestionOption> updateOption(Long mcqId, Long optionId, QuestionOption questionOption) {

         // Find the MCQ to be updated
        McqQuestion mcqQuestion = mcqRepository.findById(mcqId)
                .orElseThrow(() -> new IllegalArgumentException("Mcq question not found"));

        //Find option to be updated
        QuestionOption toBeUpdatedOption = mcqQuestion.getOptions().stream()
                .filter(option -> option.getOptionId().equals(optionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Option ID not found"));
        
        
        if(questionOption.isCorrect())
        toBeUpdatedOption.setCorrect(questionOption.isCorrect());

        if(questionOption.getOptionText()!=null)
        toBeUpdatedOption.setOptionText(questionOption.getOptionText());

        return new ResponseEntity<>(toBeUpdatedOption, HttpStatus.OK);
    }

    public ResponseEntity<QuestionOption> deleteOption(Long mcqId, Long optionId) {

        //Find mcq
        McqQuestion mcqQuestion = mcqRepository.findById(mcqId)
                .orElseThrow(() -> new IllegalArgumentException("Mcq question not found"));


        //Find option
        QuestionOption toBeDeletedOption = mcqQuestion.getOptions().stream()
                .filter(option -> option.getOptionId().equals(optionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Option ID not found"));

        mcqQuestion.getOptions().remove(toBeDeletedOption);
        mcqRepository.save(mcqQuestion);

        questionOptionRepository.delete(toBeDeletedOption);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public McqQuestion getMcqById(Long mcqId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMcqById'");
    }

}
