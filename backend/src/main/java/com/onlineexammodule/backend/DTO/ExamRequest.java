package com.onlineexammodule.backend.DTO;
import java.time.LocalDateTime;
import java.util.List;

public class ExamRequest {

    // Exam Metadata
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int duration;
    private int mcqPassingScore;  // Passing score specific to MCQs
    
    private String examinerEmail;  // Email of the examiner creating the exam

    // Exam Questions
    private List<Long> mcqQuestionIds;  // List of selected MCQ question IDs
    private List<Long> programmingQuestionIds;  // List of selected programming question IDs

    // Examinees Assignment Options
    private boolean assignToAllExaminees;  // If true, auto-assign exam to all examinees under the examiner
    private List<Long> assignedExamineeIds;  // List of specific examinee IDs if assignToAllExaminees is false

    // Getters and Setters
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMcqPassingScore() {
        return mcqPassingScore;
    }

    public void setMcqPassingScore(int mcqPassingScore) {
        this.mcqPassingScore = mcqPassingScore;
    }

    public String getExaminerEmail() {
        return examinerEmail;
    }

    public void setExaminerEmail(String examinerEmail) {
        this.examinerEmail = examinerEmail;
    }

    public List<Long> getMcqQuestionIds() {
        return mcqQuestionIds;
    }

    public void setMcqQuestionIds(List<Long> mcqQuestionIds) {
        this.mcqQuestionIds = mcqQuestionIds;
    }

    public List<Long> getProgrammingQuestionIds() {
        return programmingQuestionIds;
    }

    public void setProgrammingQuestionIds(List<Long> programmingQuestionIds) {
        this.programmingQuestionIds = programmingQuestionIds;
    }

    public boolean isAssignToAllExaminees() {
        return assignToAllExaminees;
    }

    public void setAssignToAllExaminees(boolean assignToAllExaminees) {
        this.assignToAllExaminees = assignToAllExaminees;
    }

    public List<Long> getAssignedExamineeIds() {
        return assignedExamineeIds;
    }

    public void setAssignedExamineeIds(List<Long> assignedExamineeIds) {
        this.assignedExamineeIds = assignedExamineeIds;
    }
}
