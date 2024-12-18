package com.onlineexammodule.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.onlineexammodule.backend.model.Answer;
import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.model.McqAnswer;
import com.onlineexammodule.backend.model.McqQuestion;
import com.onlineexammodule.backend.model.ProgrammingQuestion;
import com.onlineexammodule.backend.model.ProgrammingQuestionAnswer;
import com.onlineexammodule.backend.model.QuestionOption;
import com.onlineexammodule.backend.repo.AnswerRepository;
import com.onlineexammodule.backend.repo.ExamRepository;
// import com.onlineexammodule.backend.model.Examiner;
import com.onlineexammodule.backend.repo.ExamineeRepository;
// import org.springframework.security.core.AuthenticationException;
import com.onlineexammodule.backend.repo.McqAnswerRepository;
import com.onlineexammodule.backend.repo.McqRepository;
import com.onlineexammodule.backend.repo.ProgrammingAnswerRepository;
import com.onlineexammodule.backend.repo.ProgrammingRepository;

@Service
public class ExamineeService {

    @Autowired
    private JWTService jwtService;

    private final ExamineeRepository examineeRepository;
    private final AnswerRepository answerRepository;
    private final ExamRepository examRepository;
    private final McqAnswerRepository mcqAnswerRepository;
    private final ProgrammingAnswerRepository programmingAnswerRepository;

    public ExamineeService(ExamRepository examRepository, McqRepository mcqRepository,
            ProgrammingRepository programmingRepository, ExamineeRepository examineeRepository,
            AnswerRepository answerRepository, ProgrammingAnswerRepository programmingAnswerRepository,
            McqAnswerRepository mcqAnswerRepository) {
        this.examRepository = examRepository;
        this.examineeRepository = examineeRepository;
        this.answerRepository = answerRepository;
        this.mcqAnswerRepository = mcqAnswerRepository;
        this.programmingAnswerRepository = programmingAnswerRepository;
    }

    public String verify(String email) {
        Examinee examinee = examineeRepository.findByEmail(email);
        System.out.println("Finding examinee by email" + email);
        if (examinee == null) {
            System.out.println("Examinee not found for the provided email and examiner ID.");
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(email);

    }

    public List<Exam> getAllexams(Long examineeId) {

        // Get examinee and return exams.
        Examinee examinee = examineeRepository.findById(examineeId)
                .orElseThrow(() -> new IllegalArgumentException("Examinee not found"));

        return examinee.getExams();
    }

    public Long getExamineeIdFromEmail(String examineeEmail) {
        Examinee existingExaminee = examineeRepository.findByEmail(examineeEmail);
        return existingExaminee.getExamineeId();

    }

    public List<McqQuestion> getAllMcqQuestions(Long examId) {
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found or exam ID incorrect"));

        return existingExam.getMcqQuestions();
    }

    public List<ProgrammingQuestion> getAllProgrammingQuestions(Long examId) {
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found or exam ID incorrect"));

        return existingExam.getProgrammingQuestions();
    }

    public Answer submitAnswer(List<McqAnswer> mcqAnswers, List<ProgrammingQuestionAnswer> programmingQuestionAnswers,
            String examineeEmail, Long examId) {

        // Check if exam exists
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found for ID: " + examId));
        System.out.println("Exam found: " + existingExam.getExamId());
        
        List<McqQuestion> mcqQuestions=existingExam.getMcqQuestions();
        // Check if examinee exists
        Examinee existingExaminee = examineeRepository.findByEmail(examineeEmail);
        if (existingExaminee == null) {
            throw new IllegalArgumentException("Examinee not found for email: " + examineeEmail);
        }
        System.out.println("Examinee found: " + existingExaminee.getExamineeId());
        


        // Create and save a new Answer object
        Answer new_answer = new Answer();
        new_answer.setExam(existingExam);
        new_answer.setExaminee(existingExaminee);
        new_answer.setSubmitted(true);
        new_answer = answerRepository.save(new_answer); // Save parent first
        System.out.println("Answer saved with ID: " + new_answer.getAnswerId());

        // Save McqAnswers
        int mcqScore = 0;
        System.out.println("MCQ Answers length " + mcqAnswers.size());
        System.out.println("Programming answers length "+programmingQuestionAnswers.size());
        for (McqAnswer mcqAnswer : mcqAnswers) {
            McqAnswer new_mcq_answer = new McqAnswer();
            System.out.println("New Mcq Answer initialized");
            
            new_mcq_answer.setMcqQuestionId(mcqAnswer.getMcqQuestionId());

            if(mcqAnswer.getSelectedOptionId()==null)
            continue;

            new_mcq_answer.setSelectedOptionId(mcqAnswer.getSelectedOptionId());
            System.out.println("New Mcq Answer set");
            System.out.println(mcqAnswer.getMcqQuestionId());
            McqQuestion mcqQuestion = mcqQuestions.stream().filter(mcq-> mcq.getMcqId().equals(mcqAnswer.getMcqQuestionId())).findFirst().orElseThrow(() -> new IllegalArgumentException(
                        "Mcq Question not found with given ID " + mcqAnswer.getMcqQuestionId()));
            System.out.println("MCQ Question fetched" + mcqQuestion.getMcqQuestionText()+ " options size "+ mcqQuestion.getOptions().size() );
            // Set selected option
            QuestionOption option = mcqQuestion.getOptions().stream()
                    .filter(op -> op.getOptionId().equals(mcqAnswer.getSelectedOptionId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Option not found with given ID " + mcqAnswer.getSelectedOptionId()));
            if (option.isCorrect()) {
                mcqScore++;
                new_mcq_answer.setCorrect(true);
            }
            System.out.println("Saving McqAnswer: " + new_mcq_answer);
           
            new_answer.getMcqAnswers().add(new_mcq_answer);
            new_mcq_answer.setAnswer(new_answer);
            mcqAnswerRepository.save(new_mcq_answer);
        }
        new_answer.setMcqScore(mcqScore);
        new_answer.setPassed(mcqScore >= existingExam.getMcqpassingScore());

        // Save ProgrammingQuestionAnswers
        for (ProgrammingQuestionAnswer programmingQuestionAnswer : programmingQuestionAnswers) {
            ProgrammingQuestionAnswer new_pro_answer = new ProgrammingQuestionAnswer();
            
            new_pro_answer.setLanguage(programmingQuestionAnswer.getLanguage());
            new_pro_answer.setCodeSubmission(programmingQuestionAnswer.getCodeSubmission());
            System.out.println("Saving ProgrammingQuestionAnswer: " + new_pro_answer);
           
            new_answer.getProgrammingQuestionAnswers().add(new_pro_answer);
            new_pro_answer.setAnswer(new_answer);
            programmingAnswerRepository.save(new_pro_answer);
        }
        existingExam.getAnswers().add(new_answer);
        examRepository.save(existingExam);

        // Save and return
        return answerRepository.save(new_answer);
        
    }


}