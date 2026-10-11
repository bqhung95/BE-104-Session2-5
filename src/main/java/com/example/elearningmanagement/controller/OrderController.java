package com.example.elearningmanagement.controller;

import com.example.elearningmanagement.dto.request.OrderRequestDTO;
import com.example.elearningmanagement.dto.response.BaseResponse;
import com.example.elearningmanagement.dto.response.OrderDTO;
import com.example.elearningmanagement.service.OrderService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * POST /api/v1/orders
     * Tạo đơn hàng mới.
     */
    @PostMapping
    public ResponseEntity<BaseResponse<OrderDTO>> createOrder(
            @Valid @RequestBody OrderRequestDTO request) throws BadRequestException {

        OrderDTO order = orderService.createOrder(request);

        BaseResponse<OrderDTO> response = BaseResponse.created(
                "Tạo đơn hàng thành công", order);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
