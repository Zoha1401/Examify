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

    public McqService(ExamRepository examRepository, McqRepository mcqRepository,
            QuestionOptionRepository questionOptionRepository) {
        this.examRepository = examRepository;
        this.mcqRepository = mcqRepository;
        this.questionOptionRepository = questionOptionRepository;
    }

    @Transactional
    public MCQ addMcqQuestion(McqQuestion mcqQuestion, Long examId) {

        if (!examRepository.existsById(examId)) {
            throw new IllegalArgumentException("Exam not found");
        }

        // Step 2: Retrieve the Exam
        Exam exam = examRepository.getReferenceById(examId);

        // Step 3: Get options from mcqQuestion and ensure no circular save
        List<QuestionOption> getQuestionOptions = mcqQuestion.getOptions();
        System.out.println("Number of options: " + (getQuestionOptions != null ? getQuestionOptions.size() : 0));

        // Only process if options exist
        if (getQuestionOptions != null && !getQuestionOptions.isEmpty()) {
            List<QuestionOption> updatedOptions = new ArrayList<>();

            for (QuestionOption option : getQuestionOptions) {
                if (option.isCorrect() != true) {
                    option.setCorrect(false);
                }
                option.setMcqQuestion(mcqQuestion); // Associate option with mcqQuestion
                questionOptionRepository.save(option); // Save each option
                updatedOptions.add(option);
            }

            mcqQuestion.setOptions(updatedOptions); // Set updated options on mcqQuestion
        }

        // Step 4: Add the MCQ to the Exam’s list of MCQs and avoid circular references
        exam.getMcqQuestions().add(mcqQuestion);
        mcqRepository.save(mcqQuestion); // Save mcqQuestion separately to avoid cascading issues
        examRepository.save(exam); // Save exam after setting mcqQuestion

        // Step 5: Return the DTO (assuming convertToDto is defined)
        return convertToDto(mcqQuestion);
    }

    // Update MCQ
    @Transactional
    public MCQ updateMcqQuestion(Long mcqId, McqQuestion updatedMcqQuestion, Long examId) {

        // Step 1: Check if the Exam exists
        if (!examRepository.existsById(examId)) {
            throw new IllegalArgumentException("Exam not found");
        }

        // Step 2: Retrieve the existing MCQQuestion
        McqQuestion existingMcqQuestion = mcqRepository.findById(mcqId)
                .orElseThrow(() -> new IllegalArgumentException("MCQ not found"));

        // Step 3: Update MCQ properties
        existingMcqQuestion.setQuestionText(updatedMcqQuestion.getQuestionText());
        existingMcqQuestion.setCategory(updatedMcqQuestion.getCategory());
        existingMcqQuestion.setCorrectAnswer(updatedMcqQuestion.getCorrectAnswer());

        // Step 4: Update options or add new options without circular saves
        List<QuestionOption> updatedOptions = new ArrayList<>();
        for (QuestionOption option : updatedMcqQuestion.getOptions()) {
            if (option.getOptionId() != null) {
                // Option exists, update it
                QuestionOption existingOption = questionOptionRepository.findById(option.getOptionId())
                        .orElseThrow(() -> new IllegalArgumentException("Option not found"));
                existingOption.setOptionText(option.getOptionText());
                existingOption.setCorrect(option.isCorrect());
                updatedOptions.add(existingOption); // Add to list for setting on MCQ later
            } else {
                // New option, save it individually first
                option.setMcqQuestion(existingMcqQuestion); // Set the relationship to the current MCQQuestion
                questionOptionRepository.save(option); // Save new option to avoid cascading issues
                updatedOptions.add(option);
            }
        }

        // Step 5: Set the updated options list on the MCQQuestion
        existingMcqQuestion.setOptions(updatedOptions);

        // Step 6: Save the updated MCQQuestion without re-saving options
        mcqRepository.save(existingMcqQuestion);

        // Step 7: Return the updated MCQ as DTO
        return convertToDto(existingMcqQuestion);

    }

    // Delete MCQ

    private MCQ convertToDto(McqQuestion mcqQuestion) {
        MCQ mcqDto = new MCQ();
        mcqDto.setMcqId(mcqQuestion.getMcqId());
        mcqDto.setCategory(mcqQuestion.getCategory());
        mcqDto.setCorrectAnswer(mcqQuestion.getCorrectAnswer());
        mcqDto.setQuestionText(mcqQuestion.getQuestionText());
        return mcqDto;
    }

    @Transactional
    public String deleteMcqQuestion(Long mcqId, Long examId) {
        // Step 1: Retrieve the Exam
        // Step 1: Retrieve the Exam
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        // Step 2: Retrieve the MCQ Question
        McqQuestion mcqQuestion = mcqRepository.findById(mcqId)
                .orElseThrow(() -> new IllegalArgumentException("MCQ not found"));

        // Step 3: Remove the MCQ from the Exam's question list
        exam.getMcqQuestions().remove(mcqQuestion);
        examRepository.save(exam); // Save the exam to persist changes to the relationship

        // Step 4: Remove the relationship in the options
        List<QuestionOption> optionsToRemove = mcqQuestion.getOptions();

        if (optionsToRemove != null) {
            for (QuestionOption option : optionsToRemove) {
                option.setMcqQuestion(null); // Remove the link to the MCQQuestion
                questionOptionRepository.save(option); // Persist the change in the option
            }
        }

        // Step 5: Clear options list on mcqQuestion to break further associations
        mcqQuestion.setOptions(new ArrayList<>());
        mcqRepository.save(mcqQuestion); // Save mcqQuestion to reflect the cleared options list

        // Step 6: Delete the MCQ Question
        mcqRepository.delete(mcqQuestion);

        // Return success message
        return "Deleted";

    }

}
