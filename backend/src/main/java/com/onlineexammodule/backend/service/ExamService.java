package com.onlineexammodule.backend.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.onlineexammodule.backend.DTO.ExamRequest;
import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.model.Examiner;
import com.onlineexammodule.backend.model.McqQuestion;
import com.onlineexammodule.backend.model.ProgrammingQuestion;
import com.onlineexammodule.backend.repo.ExamRepository;
import com.onlineexammodule.backend.repo.ExamineeRepository;
import com.onlineexammodule.backend.repo.ExaminerRepo;
import com.onlineexammodule.backend.repo.McqRepository;
import com.onlineexammodule.backend.repo.ProgrammingRepository;


@Service
public class ExamService {
    
    private final ExaminerRepo examinerRepository;
    private final ExamineeRepository examineeRepository;

    private final ExamRepository examRepository;

    private final McqRepository mcqRepository;

    private final ProgrammingRepository programmingRepository;

     public ExamService(ExaminerRepo examinerRepository, ExamRepository examRepository, McqRepository mcqRepository, ProgrammingRepository programmingRepository, ExamineeRepository examineeRepository) {
        this.examinerRepository = examinerRepository;
        this.examRepository= examRepository;
        this.mcqRepository=mcqRepository;
        this.programmingRepository=programmingRepository;
        this.examineeRepository=examineeRepository;
     
    }

    public Exam createExam(ExamRequest exam, String examiner_email, Long examinerId) {
       Exam new_exam=new Exam();
       new_exam.setDuration(exam.getDuration());
       new_exam.setEndTime(exam.getEndTime());
       new_exam.setStartTime(exam.getStartTime());
       new_exam.setMcqpassingScore(exam.getMcqPassingScore());

       Examiner examiner=examinerRepository.findByEmail(examiner_email);;
       new_exam.setExaminer(examiner);
       
       if(exam.isAssignToAllExaminees())
       { 
        List<Examinee> examinees=examiner.getExaminees();
        for(Examinee examinee_temp : examinees)
         {
            new_exam.addExaminee(examinee_temp); //Method in Exam
         }
       }
       else if(exam.getAssignedExamineeIds()!=null)
       {
        List<Examinee> examinees=examineeRepository.findAllById(exam.getAssignedExamineeIds());
        if(examinees!=null){
            for(Examinee examinee_temp : examinees)
            {
            new_exam.addExaminee(examinee_temp); //Method in Exam
            }
        }
        
       }
      
       if(exam.getMcqQuestionIds()!=null)
       {
            List <McqQuestion> mcqQuestions=mcqRepository.findAllById(exam.getMcqQuestionIds());
            for (McqQuestion question : mcqQuestions) {
                new_exam.addMcqQuestion(question); // Method in Exam
            }
       }

       if(exam.getProgrammingQuestionIds()!=null)
       {
           List<ProgrammingQuestion> programmingQuestions=programmingRepository.findAllById(exam.getProgrammingQuestionIds());
           for (ProgrammingQuestion question : programmingQuestions) {
            new_exam.addProgrammingQuestion(question); // Method in Exam
        }
       }

       return examRepository.save(new_exam);
       
        
    }

    public String deleteExam(Long examId) {
        Exam toBeDeleteExam= examRepository.getReferenceById(examId);
        
        List<Examinee> exam_toBeDelete_examinees=toBeDeleteExam.getExaminees();
        if(exam_toBeDelete_examinees!=null){
          for(Examinee examinee_temp:exam_toBeDelete_examinees){
            examinee_temp.removeExam(toBeDeleteExam);
           }
        }
        List<McqQuestion> mcqQuestions=toBeDeleteExam.getMcqQuestions();
        if(mcqQuestions!=null){
            for(McqQuestion mcq_temp:mcqQuestions){
                mcq_temp.removeExam(toBeDeleteExam);
               }
        }

        List<ProgrammingQuestion> programmingQuestions=toBeDeleteExam.getProgrammingQuestions();
        if(programmingQuestions!=null){
            for(ProgrammingQuestion programming_temp:programmingQuestions){
                programming_temp.removeExam(toBeDeleteExam);
               }
        }

        Examiner examiner = toBeDeleteExam.getExaminer();
        if (examiner != null) {
            examiner.getExams().remove(toBeDeleteExam);
        }
        examRepository.delete(toBeDeleteExam);

        return "Exam is deleted successfully";

    }

    public Exam updateExam(ExamRequest exam, Long examId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateExam'");
    }

// public Exam createExamWithQuestions(Exam exam, List<Long> mcqQuestionIds, List<Long> programmingQuestionIds) {
//     // Retrieve MCQs and programming questions based on IDs
//     List<McqQuestion> mcqs = mcqRepository.findAllById(mcqQuestionIds);
//     List<ProgrammingQuestion> programmingQuestions = programmingRepository.findAllById(programmingQuestionIds);

//     // Add questions to the exam
//     exam.setMcqQuestions(mcqs);
//     exam.setProgrammingQuestions(programmingQuestions);

//     return examRepository.save(exam);
// }

    

}
