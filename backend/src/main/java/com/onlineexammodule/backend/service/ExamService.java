package com.onlineexammodule.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.onlineexammodule.backend.DTO.ExamRequest;
import com.onlineexammodule.backend.model.Answer;
import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.model.Examiner;
import com.onlineexammodule.backend.model.McqQuestion;
import com.onlineexammodule.backend.model.ProgrammingQuestion;
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
    private final ExamRepository examRepository;

    private final McqRepository mcqRepository;

    private final ProgrammingRepository programmingRepository;

    public ExamService(ExaminerRepo examinerRepository, ExamRepository examRepository, McqRepository mcqRepository,
            ProgrammingRepository programmingRepository, ExamineeRepository examineeRepository,
            AnswerRepository answerRepository, ProgrammingAnswerRepository programmingAnswerRepository,
            McqAnswerRepository mcqAnswerRepository) {
        this.examinerRepository = examinerRepository;
        this.examRepository = examRepository;
        this.mcqRepository = mcqRepository;
        this.programmingRepository = programmingRepository;
        this.examineeRepository = examineeRepository;
    }

    public Exam createExam(ExamRequest exam, String examiner_email) {

        // Setting exam
        Exam new_exam = new Exam();
        new_exam.setDuration(exam.getDuration());
        new_exam.setEndTime(exam.getEndTime());
        new_exam.setStartTime(exam.getStartTime());
        new_exam.setMcqpassingScore(exam.getMcqpassingScore());

        // Finding email of examiner
        Examiner examiner = examinerRepository.findByEmail(examiner_email);
        ;
        new_exam.setExaminer(examiner);
        new_exam.setAssignToAllExaminees(exam.isAssignToAllExaminees());
        // Option for examiner to create an exam for already existing examinee or not
        // all examinees.
        System.out.println("isAssign" + exam.isAssignToAllExaminees());
        System.out.println("size" + examiner.getExaminees().size());
        if (exam.isAssignToAllExaminees()) {
            List<Examinee> examinees = examiner.getExaminees();
            for (Examinee examinee_temp : examinees) {
                new_exam.addExaminee(examinee_temp); // Method in Exam
                examinee_temp.getExams().add(new_exam);
            }
        } else if (exam.getAssignedExamineeIds() != null) {
            List<Examinee> examinees = examineeRepository.findAllById(exam.getAssignedExamineeIds());
            if (examinees != null) {
                for (Examinee examinee_temp : examinees) {
                    new_exam.addExaminee(examinee_temp); // Method in Exam
                    examinee_temp.getExams().add(new_exam);
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

        // List<Answer> answers=toBeDeleteExam.getAnswers();
        // if(answers!=null){
        // for(Answer answer:answers){
        // answer.removeExam(toBeDeleteExam);

        // }
        // }

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

        if (examRequest.getMcqpassingScore() != 0) {
            existingExam.setMcqpassingScore(examRequest.getMcqpassingScore());
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

    public Exam getExamById(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam does not exist, Incorrect exam Id"));

        return exam;

    }

    public List<Examinee> getPassedExaminees(Long examId) {

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam does not exist, Incorrect exam Id"));

        List<Answer> answers = exam.getAnswers();
        int mcqPassingScore = exam.getMcqpassingScore();
        List<Examinee> examinees = new ArrayList<>();
        // Find passed answers and get the examinee from there
        for (Answer answer : answers) {

            if (answer.getMcqScore() >= mcqPassingScore) {
                examinees.add(answer.getExaminee());
            }
        }

        return examinees;

        // Get exam
        // Get answers
        // Iterate, find passed answers, fetch their examinee and store and send
    }

    public List<Examinee> getExaminees(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam does not exist, Incorrect exam Id"));

        return exam.getExaminees();
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
