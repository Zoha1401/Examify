package com.onlineexammodule.backend.DTO;

import java.util.List;

import com.onlineexammodule.backend.model.TestCase;

public class ProgrammingAnswerDTO {

     private Long pqAnswerId;              
    private String programmingQuestionText;      // Text of the question
    private List<TestCase> testCases; 
    private String codeSubmission;
    
    public Long getPqAnswerId() {
        return pqAnswerId;
    }
    public void setPqAnswerId(Long pqAnswerId) {
        this.pqAnswerId = pqAnswerId;
    }
    public String getProgrammingQuestionText() {
        return programmingQuestionText;
    }
    public void setProgrammingQuestionText(String programmingQuestionText) {
        this.programmingQuestionText = programmingQuestionText;
    }
    public List<TestCase> getTestCases() {
        return testCases;
    }
    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }
    public String getCodeSubmission() {
        return codeSubmission;
    }
    public void setCodeSubmission(String codeSubmission) {
        this.codeSubmission = codeSubmission;
    }
    
}