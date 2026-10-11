package com.example.elearningmanagement.repository;


import com.example.elearningmanagement.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}
