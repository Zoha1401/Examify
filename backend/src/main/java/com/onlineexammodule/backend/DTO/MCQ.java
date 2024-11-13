package com.onlineexammodule.backend.DTO;

import java.util.ArrayList;
import java.util.List;

import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.QuestionOption;

public class MCQ {
    private Long mcqId;
    public Long getMcqId() {
        return mcqId;
    }

    public void setMcqId(Long mcqId) {
        this.mcqId = mcqId;
    }

    private String questionText;
    private String correctAnswer;
    private String category;

    private List<QuestionOption> options=new ArrayList<>();

    private List<Exam> exams=new ArrayList<>();

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<QuestionOption> getOptions() {
        return options;
    }

    public void setOptions(List<QuestionOption> options) {
        this.options = options;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }
    
}