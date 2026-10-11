package com.example.elearningmanagement.service;

import com.example.elearningmanagement.dto.request.OrderRequestDTO;
import com.example.elearningmanagement.dto.response.OrderDTO;
import com.example.elearningmanagement.entity.CourseEntity;
import com.example.elearningmanagement.entity.OrderEntity;
import com.example.elearningmanagement.entity.StudentEntity;
import com.example.elearningmanagement.exception.DuplicateResourceException;
import com.example.elearningmanagement.exception.ResourceNotFoundException;
import com.example.elearningmanagement.repository.CourseRepository;
import com.example.elearningmanagement.repository.OrderRepository;
import com.example.elearningmanagement.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public OrderService(OrderRepository orderRepository,
                        StudentRepository studentRepository,
                        CourseRepository courseRepository) {
        this.orderRepository   = orderRepository;
        this.studentRepository = studentRepository;
        this.courseRepository  = courseRepository;
    }

    /**
     * Tạo đơn hàng — TOÀN BỘ logic nghiệp vụ nằm ở đây.
     */
    @Transactional
    public OrderDTO createOrder(OrderRequestDTO req) throws BadRequestException {

        log.info("Creating order for studentId={}, courseId={}",
                req.getStudentId(), req.getCourseId());

        // ① Kiểm tra student tồn tại
        StudentEntity student = studentRepository.findById(req.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student", "id", req.getStudentId()));

        // ② Kiểm tra course tồn tại
        CourseEntity course = courseRepository.findById(req.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Course", "id", req.getCourseId()));

        // ③ Kiểm tra course còn mở không
        if (Boolean.FALSE.equals(course.getIsActive())) {
            throw new BadRequestException("Khóa học đã đóng, không thể đăng ký");
        }

        // ④ Kiểm tra đã đăng ký khóa học này chưa (business rule)
        if (orderRepository.existsByStudentIdAndCourseId(
                req.getStudentId(), req.getCourseId())) {
            throw new DuplicateResourceException(
                    "Bạn đã đăng ký khóa học này rồi");
        }

        // ⑤ Tính tổng tiền (hiện tại = giá course; sau này có thể có giảm giá)
        java.math.BigDecimal totalPrice = course.getPrice();

        // ⑥ Tạo Entity
        OrderEntity order = new OrderEntity();
        order.setOrderCode(generateOrderCode());
        order.setStudent(student);
        order.setCourse(course);
        order.setTotalPrice(totalPrice);
        order.setStatus("PAID");
        order.setCreatedAt(java.time.LocalDateTime.now());

        // ⑦ Lưu DB
        OrderEntity saved = orderRepository.save(order);
        log.info("Order created successfully with id={}, code={}",
                saved.getId(), saved.getOrderCode());

        // ⑧ Map Entity → DTO để trả về
        return toDTO(saved);
    }

    // ===== Helper methods =====

    private String generateOrderCode() {
        String datePart = java.time.LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long seq = System.currentTimeMillis() % 100000;
        return String.format("ORD-%s-%05d", datePart, seq);
    }

    private OrderDTO toDTO(OrderEntity e) {
        OrderDTO dto = new OrderDTO();
        dto.setId(e.getId());
        dto.setOrderCode(e.getOrderCode());
        dto.setStudentId(e.getStudent().getId());
        dto.setStudentName(e.getStudent().getFullName());
        dto.setCourseId(e.getCourse().getId());
        dto.setCourseName(e.getCourse().getTitle());
        dto.setTotalPrice(e.getTotalPrice());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setStatus(e.getStatus());
        return dto;
    }
}
