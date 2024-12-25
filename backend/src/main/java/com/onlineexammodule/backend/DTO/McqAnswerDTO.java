package com.onlineexammodule.backend.DTO;

import java.util.List;

import com.onlineexammodule.backend.model.QuestionOption;

public class McqAnswerDTO {
    private Long mcqId;               // ID of the MCQ question
    private String questionText;      // Text of the question
    private List<QuestionOption> options; // List of options for the question
    private Long selectedOptionId;    // Option selected by the user
    private boolean isCorrect;
    
    public Long getMcqId() {
        return mcqId;
    }
    public void setMcqId(Long mcqId) {
        this.mcqId = mcqId;
    }
    public String getQuestionText() {
        return questionText;
    }
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    public List<QuestionOption> getOptions() {
        return options;
    }
    public void setOptions(List<QuestionOption> options) {
        this.options = options;
    }
    public Long getSelectedOptionId() {
        return selectedOptionId;
    }
    public void setSelectedOptionId(Long selectedOptionId) {
        this.selectedOptionId = selectedOptionId;
    }
    public boolean isCorrect() {
        return isCorrect;
    }
    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
