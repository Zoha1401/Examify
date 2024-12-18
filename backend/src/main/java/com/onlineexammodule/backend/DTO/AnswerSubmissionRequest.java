package com.onlineexammodule.backend.DTO;

import java.util.List;

import com.onlineexammodule.backend.model.McqAnswer;
import com.onlineexammodule.backend.model.ProgrammingQuestionAnswer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerSubmissionRequest {
    private List<McqAnswer> mcqAnswers;
    private List<ProgrammingQuestionAnswer> programmingQuestionAnswers;
    public List<McqAnswer> getMcqAnswers() {
        return mcqAnswers;
    }
    public void setMcqAnswers(List<McqAnswer> mcqAnswers) {
        this.mcqAnswers = mcqAnswers;
    }
    public List<ProgrammingQuestionAnswer> getProgrammingQuestionAnswers() {
        return programmingQuestionAnswers;
    }
    public void setProgrammingQuestionAnswers(List<ProgrammingQuestionAnswer> programmingQuestionAnswers) {
        this.programmingQuestionAnswers = programmingQuestionAnswers;
    }
}
