package com.java.expensetrackerbackend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseResponse {
    private Long id;
    private String expenseName;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
}