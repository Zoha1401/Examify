package com.onlineexammodule.backend.service;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
        mcqQuestion.getExams().add(exam);
        mcqRepository.save(mcqQuestion); 
        examRepository.save(exam); 

      
        return convertToDto(mcqQuestion);
    }

    // Update MCQ
    @Transactional
    public McqQuestion updateMcqQuestion(Long mcqId, McqQuestion updatedMcqQuestion, Long examId) {
        
        // To reduce redundancy, Multiple mcqs are shared between exams. So when
        // examiner updates the mcq
        // I have created the mechanism such that, a new copy is created. and the
        // already existing mcq is removed from that exam.

        // Check if exam exists
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        // Fetch the original MCQ
        McqQuestion originalMcq = mcqRepository.findById(mcqId)
                .orElseThrow(() -> new IllegalArgumentException("MCQ not found"));

        // Disassociate the original MCQ from the exam
        exam.getMcqQuestions().remove(originalMcq);
        originalMcq.getExams().remove(exam);

        // Create a new custom MCQ
        McqQuestion customMcq = new McqQuestion();
        customMcq.setMcqQuestionText(updatedMcqQuestion.getMcqQuestionText());
        customMcq.setCorrectAnswer(updatedMcqQuestion.getCorrectAnswer());
        customMcq.setCategory(updatedMcqQuestion.getCategory());
        customMcq.setDifficulty(updatedMcqQuestion.getDifficulty());

        // Copy options
        for (QuestionOption option : updatedMcqQuestion.getOptions()) {
            QuestionOption copiedOption = new QuestionOption();
            copiedOption.setOptionText(option.getOptionText());
            copiedOption.setCorrect(option.isCorrect());
            copiedOption.setMcqQuestion(customMcq);
            customMcq.getOptions().add(copiedOption);
        }

        // Save the custom MCQ and associate it with the exam
        mcqRepository.save(customMcq);
        exam.addMcqQuestion(customMcq);
        examRepository.save(exam);

        return customMcq;
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

    // public ResponseEntity<QuestionOption> addOption(Long mcqId, QuestionOption questionOption) {
    //     // Find the MCQ to be updated
    //     McqQuestion mcqQuestion = mcqRepository.findById(mcqId)
    //             .orElseThrow(() -> new IllegalArgumentException("Mcq question not found"));

    //     // Create a new option
    //     mcqQuestion.getOptions().add(questionOption);
    //     questionOption.setMcqQuestion(mcqQuestion);
    //     questionOptionRepository.save(questionOption);
    //     mcqRepository.save(mcqQuestion);

    //     return new ResponseEntity<>(questionOption, HttpStatus.OK);
    // }

    // public ResponseEntity<QuestionOption> updateOption(Long mcqId, Long optionId, QuestionOption questionOption) {

    //      // Find the MCQ to be updated
    //     McqQuestion mcqQuestion = mcqRepository.findById(mcqId)
    //             .orElseThrow(() -> new IllegalArgumentException("Mcq question not found"));

    //     //Find option to be updated
    //     QuestionOption toBeUpdatedOption = mcqQuestion.getOptions().stream()
    //             .filter(option -> option.getOptionId().equals(optionId))
    //             .findFirst()
    //             .orElseThrow(() -> new IllegalArgumentException("Option ID not found"));
        
        
    //     if(questionOption.isCorrect())
    //     toBeUpdatedOption.setCorrect(questionOption.isCorrect());

    //     if(questionOption.getOptionText()!=null)
    //     toBeUpdatedOption.setOptionText(questionOption.getOptionText());

    //     return new ResponseEntity<>(toBeUpdatedOption, HttpStatus.OK);
    // }

    // public ResponseEntity<QuestionOption> deleteOption(Long mcqId, Long optionId) {

    //     //Find mcq
    //     McqQuestion mcqQuestion = mcqRepository.findById(mcqId)
    //             .orElseThrow(() -> new IllegalArgumentException("Mcq question not found"));


    //     //Find option
    //     QuestionOption toBeDeletedOption = mcqQuestion.getOptions().stream()
    //             .filter(option -> option.getOptionId().equals(optionId))
    //             .findFirst()
    //             .orElseThrow(() -> new IllegalArgumentException("Option ID not found"));

    //     mcqQuestion.getOptions().remove(toBeDeletedOption);
    //     mcqRepository.save(mcqQuestion);

    //     questionOptionRepository.delete(toBeDeletedOption);
    //     return new ResponseEntity<>(HttpStatus.OK);
    // }

   
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

    public List<McqQuestion> getAllMcqQuestions(Long examId) {
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found or exam ID incorrect"));

        return existingExam.getMcqQuestions();
    }

    @Transactional
    public List<McqQuestion> addMcqQuestionList(Long examId, List<McqQuestion> listMcqs) {
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found or exam ID incorrect"));

        System.out.println("Adding mcqs to exam and vice versa");

        for (McqQuestion mcqQuestion : listMcqs) {
            // Save the MCQ question to the repository

            boolean alreadyPresent = existingExam.getMcqQuestions().stream()
            .anyMatch(q -> q.getMcqId()==mcqQuestion.getMcqId());

            if(!alreadyPresent){
            McqQuestion savedMcq = mcqRepository.save(mcqQuestion);

            // Update relationships
            savedMcq.getExams().add(existingExam);
            existingExam.getMcqQuestions().add(savedMcq);
            }
        }

        examRepository.save(existingExam);

        return existingExam.getMcqQuestions();
    }
   

     public List<McqQuestion> getMcqTechnical(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam does not exist, Incorrect exam Id"));
        
                //Get technical mcqs only
        List<McqQuestion> mcqQuestions=exam.getMcqQuestions().stream().filter(mcqQuestion-> "Technical".equals(mcqQuestion.getCategory()))
        .collect(Collectors.toList());

        return mcqQuestions;
    }

    public List<McqQuestion> getMcqAptitude(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam does not exist, Incorrect exam Id"));

                //Get Aptitude mcqs
        List<McqQuestion> mcqQuestions=exam.getMcqQuestions().stream().filter(mcqQuestion-> "Aptitude".equals(mcqQuestion.getCategory()))
                .collect(Collectors.toList());
        
        return mcqQuestions;
    }

    @SuppressWarnings("null")
    @Transactional
    public String importMcqQuestionData(MultipartFile file, Long examId) throws Exception {

        // Fetch Exam
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam does not exist, Incorrect exam Id"));
    
        if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            throw new IllegalArgumentException("Invalid file format. Please upload an Excel file.");
        }
    
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
    
        int addedRows = 0;
        int duplicateRows = 0;
    
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row
            if (isRowEmpty(row)) {
                System.out.println("Skipping empty row: " + row.getRowNum());
                continue; // Skip to the next row
            }
    
            // Fetch row-wise all the cells
            String mcqQuestionText = getCellValueAsString(row.getCell(0));
            String correctOption = getCellValueAsString(row.getCell(5));
            String category = getCellValueAsString(row.getCell(6));
            String difficulty = getCellValueAsString(row.getCell(7));
    
            // Check if this question already exists in the exam
            boolean isDuplicate = exam.getMcqQuestions().stream()
                    .anyMatch(q -> q.getMcqQuestionText().equalsIgnoreCase(mcqQuestionText));
            if (isDuplicate) {
                duplicateRows++;
                continue; // Skip duplicate question
            }
    
            // Create a new MCQ question
            McqQuestion question = new McqQuestion();
            question.setMcqQuestionText(mcqQuestionText);
            question.setCategory(category);
            question.setDifficulty(difficulty);
    
            // Create and save options
            List<QuestionOption> options = new ArrayList<>();
            for (int i = 1; i <= 4; i++) {
                String optionText = getCellValueAsString(row.getCell(i));
    
                QuestionOption option = new QuestionOption();
                option.setOptionText(optionText);
                option.setMcqQuestion(question);
    
                if (optionText.equals(correctOption)) {
                    option.setCorrect(true);
                }
    
                questionOptionRepository.save(option); // Save transient option
                options.add(option);
            }
    
            question.setOptions(options);
    
            // Save the question
            mcqRepository.save(question);
    
            // Associate question with exam
            exam.getMcqQuestions().add(question);
            question.getExams().add(exam);
    
            addedRows++;
        }
    
        workbook.close();
    
        // Save exam to persist relationships
        examRepository.save(exam);
    
        return String.format("Import completed: %d questions added, %d duplicates skipped.", addedRows, duplicateRows);
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
    
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellValueAsString(cell);
                if (value != null && !value.trim().isEmpty()) {
                    return false; // Row has at least one non-empty cell
                }
            }
        }
        return true; // Row is empty
    }
    

    }
