package com.onlineexammodule.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.onlineexammodule.backend.DTO.ExamRequest;
import com.onlineexammodule.backend.model.Answer;
import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.model.Examiner;
import com.onlineexammodule.backend.model.McqAnswer;
import com.onlineexammodule.backend.model.McqQuestion;
import com.onlineexammodule.backend.model.ProgrammingQuestion;
import com.onlineexammodule.backend.model.ProgrammingQuestionAnswer;
import com.onlineexammodule.backend.repo.AnswerRepository;
import com.onlineexammodule.backend.repo.ExamRepository;
import com.onlineexammodule.backend.repo.ExamineeRepository;
import com.onlineexammodule.backend.repo.ExaminerRepo;
import com.onlineexammodule.backend.repo.McqAnswerRepository;
import com.onlineexammodule.backend.repo.McqRepository;
import com.onlineexammodule.backend.repo.ProgrammingAnswerRepository;
import com.onlineexammodule.backend.repo.ProgrammingRepository;

@Service
public class ExamService {

    private final ExaminerRepo examinerRepository;
    private final ExamineeRepository examineeRepository;
    private final AnswerRepository answerRepository;
    private final ExamRepository examRepository;

    private final McqRepository mcqRepository;

    private final ProgrammingRepository programmingRepository;
    private final McqAnswerRepository mcqAnswerRepository;
    private final ProgrammingAnswerRepository programmingAnswerRepository;

    public ExamService(ExaminerRepo examinerRepository, ExamRepository examRepository, McqRepository mcqRepository,
            ProgrammingRepository programmingRepository, ExamineeRepository examineeRepository,
            AnswerRepository answerRepository, ProgrammingAnswerRepository programmingAnswerRepository,
            McqAnswerRepository mcqAnswerRepository) {
        this.examinerRepository = examinerRepository;
        this.examRepository = examRepository;
        this.mcqRepository = mcqRepository;
        this.programmingRepository = programmingRepository;
        this.examineeRepository = examineeRepository;
        this.answerRepository = answerRepository;
        this.mcqAnswerRepository = mcqAnswerRepository;
        this.programmingAnswerRepository = programmingAnswerRepository;
    }

    public Exam createExam(ExamRequest exam, String examiner_email) {

        // Setting exam
        Exam new_exam = new Exam();
        new_exam.setDuration(exam.getDuration());
        new_exam.setEndTime(exam.getEndTime());
        new_exam.setStartTime(exam.getStartTime());
        new_exam.setMcqpassingScore(exam.getMcqPassingScore());

        // Finding email of examiner
        Examiner examiner = examinerRepository.findByEmail(examiner_email);
        ;
        new_exam.setExaminer(examiner);

        // Option for examiner to create an exam for already existing examinee or not
        // all examinees.
        if (exam.isAssignToAllExaminees()) {
            List<Examinee> examinees = examiner.getExaminees();
            for (Examinee examinee_temp : examinees) {
                new_exam.addExaminee(examinee_temp); // Method in Exam
            }
        } else if (exam.getAssignedExamineeIds() != null) {
            List<Examinee> examinees = examineeRepository.findAllById(exam.getAssignedExamineeIds());
            if (examinees != null) {
                for (Examinee examinee_temp : examinees) {
                    new_exam.addExaminee(examinee_temp); // Method in Exam
                }
            }

        }
        // Create exam with already existing mcq.
        if (exam.getMcqQuestionIds() != null) {
            List<McqQuestion> mcqQuestions = mcqRepository.findAllById(exam.getMcqQuestionIds());
            for (McqQuestion question : mcqQuestions) {
                new_exam.addMcqQuestion(question); // Method in Exam
                question.getExams().add(new_exam);
            }
        }

        // Create exam with already existing programming questions
        if (exam.getProgrammingQuestionIds() != null) {
            List<ProgrammingQuestion> programmingQuestions = programmingRepository
                    .findAllById(exam.getProgrammingQuestionIds());
            for (ProgrammingQuestion question : programmingQuestions) {
                new_exam.addProgrammingQuestion(question); // Method in Exam
                question.getExams().add(new_exam);
            }
        }

        // save
        return examRepository.save(new_exam);

    }

    public String deleteExam(Long examId) {

        // Find exam to be deleted
        Exam toBeDeleteExam = examRepository.getReferenceById(examId);

        // Remove exam from examinee
        List<Examinee> exam_toBeDelete_examinees = toBeDeleteExam.getExaminees();
        if (exam_toBeDelete_examinees != null) {
            for (Examinee examinee_temp : exam_toBeDelete_examinees) {
                examinee_temp.removeExam(toBeDeleteExam);
            }
        }

        // Remove exam relation with MCQ. (Check McqQuestion)
        List<McqQuestion> mcqQuestions = toBeDeleteExam.getMcqQuestions();
        if (mcqQuestions != null) {
            for (McqQuestion mcq_temp : mcqQuestions) {
                mcq_temp.removeExam(toBeDeleteExam);
            }
        }

        // Remove Exam relation with programming
        List<ProgrammingQuestion> programmingQuestions = toBeDeleteExam.getProgrammingQuestions();
        if (programmingQuestions != null) {
            for (ProgrammingQuestion programming_temp : programmingQuestions) {
                programming_temp.removeExam(toBeDeleteExam);
            }
        }

        // Remove exam from examiner
        Examiner examiner = toBeDeleteExam.getExaminer();
        if (examiner != null) {
            examiner.getExams().remove(toBeDeleteExam);
        }
        examRepository.delete(toBeDeleteExam);

        return "Exam is deleted successfully";

    }

    public Exam updateExam(ExamRequest examRequest, Long examId) {

        // Find exam
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found or exam ID incorrect"));

        // Update
        if (examRequest.getDuration() != 0) {
            existingExam.setDuration(examRequest.getDuration());
        }

        if (examRequest.getMcqPassingScore() != 0) {
            existingExam.setMcqpassingScore(examRequest.getMcqPassingScore());
        }

        if (examRequest.getStartTime() != null) {
            existingExam.setStartTime(examRequest.getStartTime());
        }
        if (examRequest.getEndTime() != null) {
            existingExam.setEndTime(examRequest.getEndTime());
        }

        if (examRequest.getAssignedExamineeIds() != null && !examRequest.getAssignedExamineeIds().isEmpty()) {
            List<Examinee> examinees = examineeRepository.findAllById(examRequest.getAssignedExamineeIds());
            existingExam.setExaminees(examinees);
        }

        if (examRequest.getMcqQuestionIds() != null && !examRequest.getMcqQuestionIds().isEmpty()) {
            List<McqQuestion> mcqQuestions = mcqRepository.findAllById(examRequest.getMcqQuestionIds());
            existingExam.setMcqQuestions(mcqQuestions);
        }

        if (examRequest.getProgrammingQuestionIds() != null && !examRequest.getProgrammingQuestionIds().isEmpty()) {
            List<ProgrammingQuestion> programmingQuestions = programmingRepository
                    .findAllById(examRequest.getProgrammingQuestionIds());
            existingExam.setProgrammingQuestions(programmingQuestions);
        }

        return examRepository.save(existingExam);
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

    public Answer submitAnswer(Answer answer, Long examineeId, Long examId) {

        // Check in exam table if exam exists.
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found or exam ID incorrect"));

        // Check if examinee exists
        Examinee existingExaminee = examineeRepository.findById(examineeId)
                .orElseThrow(() -> new IllegalArgumentException("Examinee not found"));

        // Check if examinee exists in that particular exam
        if (!existingExam.getExaminees().contains(existingExaminee))
            throw new IllegalArgumentException("Examinee not there for exam");

        // Creating a new answer object
        Answer new_answer = new Answer();

        // Setting all values
        new_answer.setExam(existingExam);
        new_answer.setExaminee(existingExaminee);
        new_answer.setMcqScore(answer.getMcqScore());
        new_answer.setSubmitted(answer.isSubmitted());
        new_answer.setPassed(answer.isPassed());
        new_answer.setSubmitDateTime(answer.getSubmitDateTime());

        // Save answer for mcq
        for (McqAnswer mcqAnswer : answer.getMcqAnswers()) {
            McqAnswer new_mcq_answer = new McqAnswer();
            new_mcq_answer.setAnswer(new_answer);
            McqQuestion mcqQuestion = mcqRepository.getReferenceById(mcqAnswer.getMcqQuestionId());
            new_mcq_answer.setMcqQuestion(mcqQuestion);
            new_mcq_answer.setSelectedQuestionOption(mcqAnswer.getSelectedQuestionOption());

            mcqAnswerRepository.save(mcqAnswer);
        }

        // save answer for prograamming.
        for (ProgrammingQuestionAnswer programmingQuestionAnswer : answer.getProgrammingQuestionAnswers()) {
            ProgrammingQuestionAnswer new_pro_answer = new ProgrammingQuestionAnswer();
            new_pro_answer.setAnswer(new_answer);
            new_pro_answer.setCodeSubmission(programmingQuestionAnswer.getCodeSubmission());
            new_pro_answer.setProgrammingQuestion(
                    programmingRepository.getReferenceById(programmingQuestionAnswer.getProgrammingQuestionId()));

            programmingAnswerRepository.save(new_pro_answer);
        }
        // save and return
        return answerRepository.save(new_answer);

    }

    public List<McqQuestion> addMcqQuestionList(Long examId, List<McqQuestion> listMcqs) {
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found or exam ID incorrect"));

        System.out.println("Adding mcqs to exam and vice versa");

        for (McqQuestion mcqQuestion : listMcqs) {
            // Save the MCQ question to the repository
            McqQuestion savedMcq = mcqRepository.save(mcqQuestion);

            // Update relationships
            savedMcq.getExams().add(existingExam);
            existingExam.getMcqQuestions().add(savedMcq);
        }

        examRepository.save(existingExam);

        return existingExam.getMcqQuestions();
    }

    public List<ProgrammingQuestion> addProgrammingQuestionList(Long examId, List<ProgrammingQuestion> listPro) {
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found or exam ID incorrect"));

        for (ProgrammingQuestion programmingQuestion : listPro) {

            ProgrammingQuestion savedPro = programmingRepository.save(programmingQuestion);
            savedPro.getExams().add(existingExam);
            existingExam.addProgrammingQuestion(savedPro);
        }

        return existingExam.getProgrammingQuestions();
    }

    public Exam getExamById(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam does not exist, Incorrect exam Id"));

        return exam;

    }

    public List<McqQuestion> getMcqTechnical(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam does not exist, Incorrect exam Id"));
        
        List<McqQuestion> mcqQuestions=exam.getMcqQuestions().stream().filter(mcqQuestion-> "Technical".equals(mcqQuestion.getCategory()))
        .collect(Collectors.toList());

        return mcqQuestions;
    }

    public List<McqQuestion> getMcqAptitude(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam does not exist, Incorrect exam Id"));

        List<McqQuestion> mcqQuestions=exam.getMcqQuestions().stream().filter(mcqQuestion-> "Aptitude".equals(mcqQuestion.getCategory()))
                .collect(Collectors.toList());
        
        return mcqQuestions;
    }

    // public Exam createExamWithQuestions(Exam exam, List<Long> mcqQuestionIds,
    // List<Long> programmingQuestionIds) {
    // // Retrieve MCQs and programming questions based on IDs
    // List<McqQuestion> mcqs = mcqRepository.findAllById(mcqQuestionIds);
    // List<ProgrammingQuestion> programmingQuestions =
    // programmingRepository.findAllById(programmingQuestionIds);

    // // Add questions to the exam
    // exam.setMcqQuestions(mcqs);
    // exam.setProgrammingQuestions(programmingQuestions);

    // return examRepository.save(exam);
    // }

}
