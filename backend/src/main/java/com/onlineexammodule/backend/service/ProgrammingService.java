package com.onlineexammodule.backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.ProgrammingQuestion;
import com.onlineexammodule.backend.model.TestCase;
import com.onlineexammodule.backend.repo.ExamRepository;
import com.onlineexammodule.backend.repo.ProgrammingRepository;
import com.onlineexammodule.backend.repo.TestCaseRepository;

import jakarta.transaction.Transactional;

@Service
public class ProgrammingService {

    private final ProgrammingRepository programmingRepository;
    private final ExamRepository examRepository;
    private final TestCaseRepository testCaseRepository;

    public ProgrammingService(ProgrammingRepository programmingRepository, ExamRepository examRepository, TestCaseRepository testCaseRepository)
    {
        this.programmingRepository=programmingRepository;
        this.examRepository=examRepository;
        this.testCaseRepository=testCaseRepository;
    }
    
    //ADD Pro question
    public ProgrammingQuestion addProgrammingQuestion(ProgrammingQuestion programmingQuestion, Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));
    
        // Set bidirectional reference
        programmingRepository.save(programmingQuestion);
        for (TestCase testCase : programmingQuestion.getTestCases()) {
            testCase.setProgrammingQuestion(programmingQuestion);
        }
    
        exam.getProgrammingQuestions().add(programmingQuestion);
        programmingQuestion.getExams().add(exam);
    
        // Save only the exam, cascading the save operation
        examRepository.save(exam);
    
        return programmingQuestion;
    }
    
    public List<TestCase> fetchTestCases(Long pro_id) {
       
        if(!programmingRepository.existsById(pro_id))
        {
            throw new IllegalArgumentException("Programming question does not exist "+pro_id);
        }
      
        ProgrammingQuestion gProgrammingQuestion=programmingRepository.getReferenceById(pro_id);

        List<TestCase> gTestCases=gProgrammingQuestion.getTestCases();

        return gTestCases;
       
       
    }
     // Update pro
    public ProgrammingQuestion updateProgrammingQuestion(ProgrammingQuestion programmingQuestion, Long examId, Long proId) {
        
        // To reduce redundancy, Multiple pro questions are shared between exams. So when
        // examiner updates the pro question
        // I have created the mechanism such that, a new copy is created. and the
        // already existing pro question is removed from that exam.

        Exam exam = examRepository.findById(examId).orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        ProgrammingQuestion originalProQ=programmingRepository.findById(proId)
        .orElseThrow(()->new ResourceAccessException("Programming question not found"));

        exam.getProgrammingQuestions().remove(originalProQ);
        originalProQ.getExams().remove(exam);

        ProgrammingQuestion editableProgrammingQuestion=new ProgrammingQuestion();
        editableProgrammingQuestion.setDifficulty(programmingQuestion.getDifficulty());
        editableProgrammingQuestion.setProgrammingQuestionText(programmingQuestion.getProgrammingQuestionText());
        if(programmingQuestion.getReferenceAnswer()!=null){
            editableProgrammingQuestion.setReferenceAnswer(programmingQuestion.getReferenceAnswer());
        }

        for(TestCase testCase:programmingQuestion.getTestCases()){
            TestCase testCase_temp=new TestCase();
            testCase_temp.setInput(testCase.getInput());
            testCase_temp.setExpectedOutput(testCase.getExpectedOutput());
            testCase_temp.setProgrammingQuestion(editableProgrammingQuestion);
            editableProgrammingQuestion.getTestCases().add(testCase_temp);

        }

        programmingRepository.save(editableProgrammingQuestion);
        exam.addProgrammingQuestion(editableProgrammingQuestion);
        examRepository.save(exam);

        return editableProgrammingQuestion;


    }
     //Delete Pro
    public ProgrammingQuestion deleteProgrammingQuestion(Long examId, Long proId) {
        Exam exam = examRepository.findById(examId).orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        ProgrammingQuestion toBeDeleted = exam.getProgrammingQuestions().stream()
            .filter(progQuestion -> progQuestion.getProgrammingQuestionId().equals(proId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Programming Question not found in this Exam"));

        
        exam.getProgrammingQuestions().remove(toBeDeleted);
        examRepository.save(exam);
        
        toBeDeleted.getExams().remove(exam);

        return toBeDeleted;
    }

    //Update TestCase

    public TestCase updateTestCase(TestCase testcase, Long examId, Long proId, Long testcaseId)
    {
        Exam exam = examRepository.findById(examId).orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        ProgrammingQuestion programmingQuestion = exam.getProgrammingQuestions().stream()
            .filter(progQuestion -> progQuestion.getProgrammingQuestionId().equals(proId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Programming Question not found in this Exam"));
        
            //fetch testcase to be updated
        TestCase toBeUpdatedTestCase = programmingQuestion.getTestCases().stream()
            .filter(tc -> tc.getTestcaseId().equals(testcaseId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Test Case not found in this Programming Question"));

            toBeUpdatedTestCase.setInput(testcase.getInput());
            toBeUpdatedTestCase.setExpectedOutput(testcase.getExpectedOutput());

        programmingRepository.save(programmingQuestion);
        return toBeUpdatedTestCase;

    }

    public TestCase addTestCase(TestCase newTestCase, Long examId, Long proId) {
      
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));
    
       
        ProgrammingQuestion programmingQuestion = exam.getProgrammingQuestions().stream()
                .filter(progQuestion -> progQuestion.getProgrammingQuestionId().equals(proId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Programming Question not found in this Exam"));
    
        programmingQuestion.getTestCases().add(newTestCase);
        programmingRepository.save(programmingQuestion);
        testCaseRepository.save(newTestCase);
        return newTestCase;
    }


    public ResponseEntity<String> deleteTestCase(Long examId, Long proId, Long testcaseId) {
     
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));
    
        ProgrammingQuestion programmingQuestion = exam.getProgrammingQuestions().stream()
                .filter(progQuestion -> progQuestion.getProgrammingQuestionId().equals(proId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Programming Question not found in this Exam"));
    

        TestCase testCaseToDelete = programmingQuestion.getTestCases().stream()
                .filter(tc -> tc.getTestcaseId().equals(testcaseId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Test Case not found in this Programming Question"));
    
       
        programmingQuestion.getTestCases().remove(testCaseToDelete);
    
        programmingRepository.save(programmingQuestion);
    
       
        testCaseRepository.deleteById(testcaseId);
        return ResponseEntity.ok("Test Case deleted successfully");
    }
    
    
    public List<ProgrammingQuestion> getProgrammingQuestionByDifficulty(String difficulty) {
       if(difficulty==null || difficulty.isEmpty())
       {
        return programmingRepository.findAll();
       }
       return programmingRepository.findAllByDifficulty(difficulty);
    }

    public String addProgrammingQuestionPool(List<ProgrammingQuestion> programmingQuestions) {
       for(ProgrammingQuestion programmingQuestion:programmingQuestions){
        programmingRepository.save(programmingQuestion);
       }

       return "Added";
    }
    

    public List<ProgrammingQuestion> getAllProgrammingQuestions(Long examId) {
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found or exam ID incorrect"));

        return existingExam.getProgrammingQuestions();
    }

    
   
    @Transactional
    public List<ProgrammingQuestion> addProgrammingQuestionList(Long examId, List<ProgrammingQuestion> listPro) {
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found or exam ID incorrect"));
    
        for (ProgrammingQuestion programmingQuestion : listPro) {
            // Check if the question already exists
            boolean alreadyPresent = existingExam.getProgrammingQuestions().stream()
                    .anyMatch(q -> q.getProgrammingQuestionId()==programmingQuestion.getProgrammingQuestionId());
    
            if (!alreadyPresent) {
                // Add only if not present
                ProgrammingQuestion proQ = programmingRepository.save(programmingQuestion);
                proQ.getExams().add(existingExam);
                existingExam.getProgrammingQuestions().add(proQ);
            }
        }
    
        // Save updated exam
        examRepository.save(existingExam);
    
        return existingExam.getProgrammingQuestions();
    }
    
    











    
}
