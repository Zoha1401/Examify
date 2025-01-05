package com.onlineexammodule.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.onlineexammodule.backend.DTO.McqAnswerDTO;
import com.onlineexammodule.backend.DTO.ProgrammingAnswerDTO;
import com.onlineexammodule.backend.model.Answer;
import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.model.McqAnswer;
import com.onlineexammodule.backend.model.McqQuestion;
import com.onlineexammodule.backend.model.ProgrammingQuestion;
import com.onlineexammodule.backend.model.ProgrammingQuestionAnswer;
import com.onlineexammodule.backend.model.QuestionOption;
import com.onlineexammodule.backend.model.TestCase;
import com.onlineexammodule.backend.repo.AnswerRepository;
import com.onlineexammodule.backend.repo.ExamineeRepository;
import com.onlineexammodule.backend.repo.McqRepository;
import com.onlineexammodule.backend.repo.ProgrammingRepository;

@Service
public class AnswerService {
    private final ExamineeRepository examineeRepository;
    private final AnswerRepository answerRepository;
    private final McqRepository mcqRepository;

    private final ProgrammingRepository programmingRepository;

    public AnswerService(ExamineeRepository examineeRepository, AnswerRepository answerRepository,
            ProgrammingRepository programmingRepository, McqRepository mcqRepository) {
        this.examineeRepository = examineeRepository;
        this.answerRepository = answerRepository;
        this.mcqRepository = mcqRepository;
        this.programmingRepository = programmingRepository;
    }

    public Answer getExamSpecificAnswers(Long examineeId, Long examId) {
        System.out.println("Examinee Id " + examineeId);
        Examinee examinee = examineeRepository.findById(examineeId)
                .orElseThrow(() -> new IllegalArgumentException("Examinee not found"));

        Answer answer = examinee.getAnswers().stream()
                .filter(ans -> ans.getExam().getExamId().equals(examId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Answer not found " + examineeId));

        return answer;
    }

    public List<McqAnswerDTO> getMcqAnswersWithDetails(Long answerId) {
        // Fetch answer
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found"));

        List<McqAnswer> mcqAnswers = answer.getMcqAnswers();

        List<McqAnswerDTO> answers = new ArrayList<>();

        // For every mcq, fetch the mcq and send dto with correct answer and marked
        // answer.
        for (McqAnswer mcqAnswer : mcqAnswers) {
            McqQuestion mcqQuestion = mcqRepository.findById(mcqAnswer.getMcqQuestionId())
                    .orElseThrow(() -> new IllegalArgumentException("MCQ question not found"));

            List<QuestionOption> options = mcqQuestion.getOptions();
            McqAnswerDTO dto = new McqAnswerDTO();
            dto.setMcqId(mcqQuestion.getMcqId());
            dto.setQuestionText(mcqQuestion.getMcqQuestionText());
            dto.setOptions(options);
            dto.setSelectedOptionId(mcqAnswer.getSelectedOptionId());
            dto.setCorrect(mcqAnswer.isCorrect());

            answers.add(dto);
        }

        return answers;
    }

    public List<ProgrammingAnswerDTO> getProgrammingAnswerDetail(Long answerId) {
        // Fetch answer
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found"));

        List<ProgrammingQuestionAnswer> programmingQuestionAnswers = answer.getProgrammingQuestionAnswers();

        List<ProgrammingAnswerDTO> answers = new ArrayList<>();

        // For every programmming answer fetch question and code submitted by the
        // student
        for (ProgrammingQuestionAnswer programmingQuestionAnswer : programmingQuestionAnswers) {
            ProgrammingQuestion programmingQuestion = programmingRepository
                    .findById(programmingQuestionAnswer.getPqId())
                    .orElseThrow(() -> new IllegalArgumentException("MCQ question not found"));

            List<TestCase> testCases = programmingQuestion.getTestCases();
            ProgrammingAnswerDTO dto = new ProgrammingAnswerDTO();
            dto.setCodeSubmission(programmingQuestionAnswer.getCodeSubmission());
            dto.setProgrammingQuestionText(programmingQuestion.getProgrammingQuestionText());
            dto.setTestCases(testCases);
            dto.setPqAnswerId(programmingQuestionAnswer.getProgrammingAnswerId());

            answers.add(dto);
        }

        return answers;

    }

}
