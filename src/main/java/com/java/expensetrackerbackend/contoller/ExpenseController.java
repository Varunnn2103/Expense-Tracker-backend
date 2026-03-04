package com.java.expensetrackerbackend.contoller;


import com.java.expensetrackerbackend.dto.*;
import com.java.expensetrackerbackend.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses(Authentication auth) {
        return ResponseEntity.ok(expenseService.getAllExpenses(auth.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable Long id, Authentication auth) {
        try {
            return ResponseEntity.ok(expenseService.getExpenseById(id, auth.getName()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createExpense(@Valid @RequestBody ExpenseRequest request, Authentication auth) {
        try {
            ExpenseResponse response = expenseService.createExpense(request, auth.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id,
                                           @Valid @RequestBody ExpenseRequest request,
                                           Authentication auth) {
        try {
            return ResponseEntity.ok(expenseService.updateExpense(id, request, auth.getName()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id, Authentication auth) {
        try {
            expenseService.deleteExpense(id, auth.getName());
            return ResponseEntity.ok(new ErrorResponse("Expense deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }

    record ErrorResponse(String message) {}
}