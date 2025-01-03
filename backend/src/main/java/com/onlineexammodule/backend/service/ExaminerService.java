package com.onlineexammodule.backend.service;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.model.Examiner;
import com.onlineexammodule.backend.repo.ExamRepository;
import com.onlineexammodule.backend.repo.ExamineeRepository;
// import com.onlineexammodule.backend.model.ExaminerPrincipal;
import com.onlineexammodule.backend.repo.ExaminerRepo;

@Service
public class ExaminerService {

    private static final Logger logger = LoggerFactory.getLogger(ExaminerService.class);
    @Autowired
    private final ExamineeRepository examineeRepository;

    private final ExamRepository examRepository;
    @Autowired
    private final ExaminerRepo examinerRepository;

    public ExaminerService(ExaminerRepo examinerRepository, ExamineeRepository examineeRepository,
            ExamRepository examRepository) {
        this.examinerRepository = examinerRepository;
        this.examineeRepository = examineeRepository;
        this.examRepository = examRepository;

    }

    @Autowired
    private AuthenticationManager authManager;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    public Examiner signInExaminer(Examiner examiner) {
        System.out.println(examiner);
        String email = examiner.getEmail();

        Examiner existingExaminer = examinerRepository.findByEmail(email);

        if (existingExaminer != null) {
            throw new IllegalArgumentException("Examiner already exists");
        }
        return examinerRepository.save(examiner);
    }

    public String verify(Examiner examiner) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(examiner.getEmail(), examiner.getPassword()));
            String token = jwtService.generateToken(examiner.getEmail());
            return token;
        } catch (AuthenticationException ex) {
            System.out.println(ex.getLocalizedMessage());
            return "Fail";
        }
    }

    @Transactional
    public Examinee addExaminee(Examinee examinee, String email) {

        // Get examiner through email extracted from token
        Examiner examiner = examinerRepository.findByEmail(email);

        if (examiner == null) {
            throw new RuntimeException("Examiner does not exist");
        }
        System.out.println("Inside Service examiner id " + examiner.getExaminerId());

        // Check if examinee is saved
        Examinee existExaminee = examineeRepository.findByEmail(examinee.getEmail());

        Examinee examineeToSave = new Examinee();
        if (existExaminee != null) {
            // Use existing examinee
            examineeToSave = existExaminee;
            logger.info("Examinee already exists with ID: {}", examineeToSave.getExamineeId());
        } else {
            // New examinee to add

            examineeToSave = examinee;
            logger.info("Creating new examinee with details: {}", examineeToSave);
        }

        // Add examiner to examinee's list of examiners if not already present
        if (!examineeToSave.getExaminers().contains(examiner)) {
            examineeToSave.getExaminers().add(examiner);
        } else {
            logger.warn("Examiner with ID {} is already associated with Examinee ID {}", examiner.getExaminerId(),
                    examineeToSave.getEmail());
        }

        // Add examinee to examiner's list if not already present
        if (!examiner.getExaminees().contains(examineeToSave)) {
            examiner.getExaminees().add(examineeToSave);
        }

        String phone = Long.toString(examinee.getPhoneNumber());
        examineeToSave.setPhoneHash(phone);
        System.out.println(examineeToSave.getPassword());

        Examinee savedExaminee = examineeRepository.save(examineeToSave);

        System.out.println("Inside Service size of examinee's examiners " + savedExaminee.getExaminers().size());
        return savedExaminee;

    }

    public Examinee deleteExaminee(Long examineeId, String examiner_email) {

        // Get examiner through email extracted from token
        Examiner examiner = examinerRepository.findByEmail(examiner_email);

        if (examiner == null) {
            throw new IllegalArgumentException("Examiner not found with email: " + examiner_email);
        }

        // Find examinee and delete
        Examinee examinee = examineeRepository.findById(examineeId)
                .orElseThrow(() -> new IllegalArgumentException("Examinee not found with ID: " + examineeId));

        examiner.removeExaminee(examinee);
        examinee.getExaminers().remove(examiner);
        examinerRepository.save(examiner);

        if (examinee.getExaminers().isEmpty()) {
            examineeRepository.delete(examinee);
            System.out.println("Deleted examinee from database as it has no other associated examiners.");
        } else {
            System.out.println(
                    "Examinee removed from examiner but not deleted as it is associated with other examiners.");
        }

        return examinee;

    }

    public Examinee updateExaminee(Examinee updatedExaminee, Long examineeId) {

        // Fetch examinee to be updated
        if (examineeId == null) {
            throw new IllegalArgumentException("Examinee ID is required for update.");
        }

        Examinee existingExaminee = examineeRepository.findById(examineeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Examinee not found with ID: " + updatedExaminee.getExamineeId()));

        // Update values if set
        if (updatedExaminee.getCollege() != null)
            existingExaminee.setCollege(updatedExaminee.getCollege());

        if (updatedExaminee.getEmail() != null)
            existingExaminee.setEmail(updatedExaminee.getEmail());

        if (updatedExaminee.getDegree() != null)
            existingExaminee.setDegree(updatedExaminee.getDegree());

        if (updatedExaminee.getYear() != null)
            existingExaminee.setYear(updatedExaminee.getYear());

        if (updatedExaminee.getPhoneNumber() != null)
            existingExaminee.setPhoneNumber(updatedExaminee.getPhoneNumber());

        return examineeRepository.save(existingExaminee);
    }

    public Examinee getExaminee(String examinee_email, String examiner_email) {

        // Get examiner by email
        Examiner examiner = examinerRepository.findByEmail(examiner_email);

        if (examiner == null) {
            throw new IllegalArgumentException("Examiner not found with email " + examiner_email);
        }

        // Get examinee from list of examiners.
        return examiner.getExaminees().stream()
                .filter(examinee -> examinee.getEmail().equals(examinee_email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Examinee not found with email " + examinee_email + " under examiner " + examiner_email));

    }

    public List<Examinee> getAllExaminee(String examiner_email) {
        Examiner examiner = examinerRepository.findByEmail(examiner_email);

        if (examiner == null) {
            throw new IllegalArgumentException("Examiner not found with email: " + examiner_email);
        }

        return examiner.getExaminees();
    }

    public Examinee getExamineeById(Long examineeId) {
        Examinee fetchExaminee = examineeRepository.findById(examineeId)
                .orElseThrow(() -> new IllegalArgumentException("Examinee not found"));

        return fetchExaminee;

    }

    public List<Exam> getAllExams(String examiner_email) {
        // Find examiner
        Examiner examiner = examinerRepository.findByEmail(examiner_email);

        if (examiner == null) {
            throw new IllegalArgumentException("Examiner not found with email: " + examiner_email);
        }

        // Fetch all his/her exams
        return examiner.getExams();
    }

    public String assignToAll(Long examId, String examiner_email) {
        Examiner examiner = examinerRepository.findByEmail(examiner_email);

        if (examiner == null) {
            throw new IllegalArgumentException("Examiner not found with email: " + examiner_email);
        }

        // Fetch exam from examiners all exams.
        Exam existingExam = examiner.getExams().stream()
                .filter(exam -> exam.getExamId() == examId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Exam not found with ID " + examId + " under examiner " + examiner_email));

        List<Examinee> examinees = examiner.getExaminees();

        // Get examinee and assign exam to all
        for (Examinee examinee : examinees) {
            if (!examinee.getExams().contains(existingExam)) { // Avoid redundant additions
                examinee.getExams().add(existingExam);
                existingExam.getExaminees().add(examinee);
                examineeRepository.save(examinee);
            }
        }

        examRepository.save(existingExam);

        return examinees.size() + " examinees added to exam ID " + examId;
    }

    public String assignToSpecificExaminee(List<Examinee> examinees, Long examId, String examiner_email) {

        // Fetch examiner
        Examiner examiner = examinerRepository.findByEmail(examiner_email);

        if (examiner == null) {
            throw new IllegalArgumentException("Examiner not found with email: " + examiner_email);
        }

        // Fetch exam from examiners all exams.
        Exam existingExam = examiner.getExams().stream()
                .filter(exam -> exam.getExamId().equals(examId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Exam not found with ID " + examId + " under examiner " + examiner_email));

        // Add exam to the list of examinees
        for (Examinee examinee : examinees) {
            Examinee existingExaminee = examineeRepository.findById(examinee.getExamineeId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Examinee not found with ID: " + examinee.getExamineeId()));

            if (!existingExaminee.getExams().contains(existingExam)) { // Avoid redundant additions
                existingExaminee.getExams().add(existingExam);
                existingExam.getExaminees().add(existingExaminee);

            }
            examineeRepository.save(existingExaminee);
        }

        // Save
        examRepository.save(existingExam);

        return examinees.size() + " examinees added to exam ID " + examId;
    }

    @SuppressWarnings("null")
    @Transactional
    public String importExamineeData(MultipartFile file, String examinerEmail) throws IOException {
        // Find the examiner
        Examiner examiner = examinerRepository.findByEmail(examinerEmail);
        if (examiner == null) {
            throw new IllegalArgumentException("Examiner not found with email: " + examinerEmail);
        }

        // Validate file format
        if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            throw new IllegalArgumentException("Invalid file format. Please upload an Excel file.");
        }

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

        int rowsProcessed = 0;
        int duplicatesSkipped = 0;

        for (Row row : sheet) {
            if (row.getRowNum() == 0)
                continue; // Skip header row

            try {
                // Safely extract data
                String email = getCellValue(row.getCell(0));
                String college = getCellValue(row.getCell(1));
                String degree = getCellValue(row.getCell(2));
                Integer year = getNumericCellValue(row.getCell(3));
                Long phoneNumber = getNumericCellValueAsLong(row.getCell(4));
                String phone = Long.toString(phoneNumber);

                if (email == null || email.isEmpty()) {
                    System.err.println("Skipping row " + row.getRowNum() + ": Email is missing or empty.");
                    continue;
                }

                // Check if the examinee already exists
                Examinee examinee = examineeRepository.findByEmail(email);
                if (examinee == null) {
                    // If not, create a new Examinee
                    examinee = new Examinee();
                    examinee.setEmail(email);
                    examinee.setCollege(college);
                    examinee.setDegree(degree);
                    examinee.setYear(year);
                    examinee.setPhoneNumber(phoneNumber);
                    examinee.setPhoneHash(phone);

                    examinee = examineeRepository.save(examinee); // Save the new Examinee
                    rowsProcessed++;
                } else {
                    System.out.println("Examinee already exists with email: " + email);
                    duplicatesSkipped++;
                }

                if (!examiner.getExaminees().contains(examinee)) {
                    examiner.getExaminees().add(examinee);
                    examinee.getExaminers().add(examiner);
                }
            } catch (Exception e) {
                System.err.println("Error processing row " + row.getRowNum() + ": " + e.getMessage());
            }
        }

        workbook.close();
        examinerRepository.save(examiner); // Save the updated Examiner

        String resultMessage = String.format(
                "Examinee import completed. Rows processed: %d, Duplicates skipped: %d, Total Examinees: %d",
                rowsProcessed, duplicatesSkipped, examiner.getExaminees().size());
        System.out.println(resultMessage);

        return resultMessage;
    }

    private String getCellValue(Cell cell) {
        if (cell == null)
            return null;
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : null;
    }

    // Helper method to safely get numeric cell value as Integer or Long
    private Integer getNumericCellValue(Cell cell) {
        if (cell == null)
            return null;
        return cell.getCellType() == CellType.NUMERIC ? (int) cell.getNumericCellValue() : null;
    }

    private Long getNumericCellValueAsLong(Cell cell) {
        if (cell == null)
            return null;
        return cell.getCellType() == CellType.NUMERIC ? (long) cell.getNumericCellValue() : null;
    }

}
