package com.java.expensetrackerbackend.service;

import com.java.expensetrackerbackend.dto.*;
import com.java.expensetrackerbackend.model.*;
import com.java.expensetrackerbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private ExpenseResponse toResponse(Expense expense) {
        ExpenseResponse res = new ExpenseResponse();
        res.setId(expense.getId());
        res.setExpenseName(expense.getExpenseName());
        res.setAmount(expense.getAmount());
        res.setDate(expense.getDate());
        res.setDescription(expense.getDescription());
        return res;
    }

    public List<ExpenseResponse> getAllExpenses(String username) {
        User user = getCurrentUser(username);
        return expenseRepository.findByUserId(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ExpenseResponse getExpenseById(Long id, String username) {
        User user = getCurrentUser(username);
        Expense expense = expenseRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        return toResponse(expense);
    }

    public ExpenseResponse createExpense(ExpenseRequest request, String username) {
        User user = getCurrentUser(username);
        Expense expense = Expense.builder()
                .expenseName(request.getExpenseName())
                .amount(request.getAmount())
                .date(LocalDate.from(request.getDate()))
                .description(request.getDescription())
                .user(user)
                .build();
        return toResponse(expenseRepository.save(expense));
    }

    public ExpenseResponse updateExpense(Long id, ExpenseRequest request, String username) {
        User user = getCurrentUser(username);
        Expense expense = expenseRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        expense.setExpenseName(request.getExpenseName());
        expense.setAmount(request.getAmount());
        expense.setDate(LocalDate.from(request.getDate()));
        expense.setDescription(request.getDescription());

        return toResponse(expenseRepository.save(expense));
    }

    public void deleteExpense(Long id, String username) {
        User user = getCurrentUser(username);
        Expense expense = expenseRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        expenseRepository.delete(expense);
    }
}