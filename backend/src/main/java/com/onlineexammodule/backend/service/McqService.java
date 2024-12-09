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
        if( updatedMcqQuestion.getMcqQuestionText()!=null)
        existingMcqQuestion.setMcqQuestionText(updatedMcqQuestion.getMcqQuestionText());

        if( updatedMcqQuestion.getMcqQuestionText()!=null)
        existingMcqQuestion.setCategory(updatedMcqQuestion.getCategory());

        if( updatedMcqQuestion.getCorrectAnswer()!=null)
        existingMcqQuestion.setCorrectAnswer(updatedMcqQuestion.getCorrectAnswer());

        //Set options
        List<QuestionOption> existingOptions = existingMcqQuestion.getOptions();
        existingOptions.clear();
        for (QuestionOption option : updatedMcqQuestion.getOptions()) {
            if (option.getOptionId() != null) {
                QuestionOption existingOption = questionOptionRepository.findById(option.getOptionId())
                        .orElseThrow(() -> new IllegalArgumentException("Option not found"));
                existingOption.setOptionText(option.getOptionText());
                existingOption.setCorrect(option.isCorrect());
                existingOptions.add(existingOption);
            } else {
                option.setMcqQuestion(existingMcqQuestion);
                questionOptionRepository.save(option);
                existingOptions.add(option);
            }
        }
    
        mcqRepository.save(existingMcqQuestion);
        return convertToDto(existingMcqQuestion);

    }

    
    @Transactional
    private MCQ convertToDto(McqQuestion mcqQuestion) {
        MCQ mcqDto = new MCQ();
        mcqDto.setMcqId(mcqQuestion.getMcqId());
        mcqDto.setCategory(mcqQuestion.getCategory());
        mcqDto.setCorrectAnswer(mcqQuestion.getCorrectAnswer());
        mcqDto.setMcqQuestionText(mcqQuestion.getMcqQuestionText());
        mcqDto.setDifficulty(mcqQuestion.getDifficulty());
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
        
        mcqQuestion.getExams().remove(exam);

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

    public List<McqQuestion> getMcqQuestionByDifficultyAndCategory(String category, String difficulty) {
       
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
      
       if(difficulty==null || difficulty.isEmpty()){
        return mcqRepository.findAllByCategory(category);
       }

       return mcqRepository.findAllByCategoryAndDifficulty(category, difficulty);
          
      
    }

    public String addMcqQuestionPool(List<McqQuestion> mcqQuestions) {
        for(McqQuestion mcqQuestion:mcqQuestions){
            mcqRepository.save(mcqQuestion);
        }
        return "Added";
    }

}
