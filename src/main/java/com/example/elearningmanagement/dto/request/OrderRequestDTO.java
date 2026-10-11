package com.example.elearningmanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;



public class OrderRequestDTO {

    @NotNull(message = "studentId không được để trống")
    @Positive(message = "studentId phải là số dương")
    private Long studentId;

    @NotNull(message = "courseId không được để trống")
    @Positive(message = "courseId phải là số dương")
    private Long courseId;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
}
