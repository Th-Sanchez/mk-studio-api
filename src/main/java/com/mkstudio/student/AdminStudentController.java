package com.mkstudio.student;

import com.mkstudio.student.dto.PagedResponse;
import com.mkstudio.student.dto.StudentDetailResponse;
import com.mkstudio.student.dto.StudentListItemResponse;
import com.mkstudio.student.dto.StudentSummaryCountsResponse;
import com.mkstudio.student.dto.UpdateAssessmentRequest;
import com.mkstudio.student.dto.UpdateStudentRequest;
import com.mkstudio.student.dto.UpdateStudentStatusRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/students")
public class AdminStudentController {

    private final StudentService studentService;

    public AdminStudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/summary")
    public StudentSummaryCountsResponse getSummary() {
        return studentService.getSummary();
    }

    @GetMapping
    public PagedResponse<StudentListItemResponse> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) StudentStatus status,
            @RequestParam(defaultValue = "false") boolean onlyHealthAlerts,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return studentService.list(search, status, onlyHealthAlerts, page, size);
    }

    @GetMapping("/{id}")
    public StudentDetailResponse getDetail(@PathVariable UUID id) {
        return studentService.getDetail(id);
    }

    @PutMapping("/{id}")
    public StudentDetailResponse updateStudent(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStudentRequest request
    ) {
        return studentService.updateStudent(id, request);
    }

    @PutMapping("/{id}/anamnesis/{anamnesisId}/assessment")
    public StudentDetailResponse updateAssessment(
            @PathVariable UUID id,
            @PathVariable UUID anamnesisId,
            @Valid @RequestBody UpdateAssessmentRequest request
    ) {
        return studentService.updateAssessment(id, anamnesisId, request);
    }

    @PatchMapping("/{id}/status")
    public StudentDetailResponse updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStudentStatusRequest request
    ) {
        return studentService.updateStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable UUID id) {
        studentService.deleteStudent(id);
    }
}
