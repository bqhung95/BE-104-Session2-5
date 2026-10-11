package com.example.elearningmanagement.repository;

import com.example.elearningmanagement.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
}
