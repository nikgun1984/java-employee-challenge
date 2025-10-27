package com.reliaquest.api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.reliaquest.api.controller.IEmployeeController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeResponse;
import com.reliaquest.api.service.EmployeeService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class EmployeeController implements IEmployeeController<EmployeeResponse, Employee> {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    
    @Override
    @GetMapping("/employees/search")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByNameSearch(@RequestParam("name") String nameFragment) {
        List<EmployeeResponse> result = employeeService.getEmployeesByNameSearch(nameFragment);
        return ResponseEntity.ok(result);
    }
    
    @Override
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable("id") String id) {
        EmployeeResponse employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @GetMapping("/employees/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        int highestSalary = employeeService.getHighestSalaryOfEmployees();
        return ResponseEntity.ok(highestSalary);
    }
    
    @Override
    @GetMapping("employees/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> top10Names = employeeService.getTopTenHighestEarningEmployeeNames();
        return ResponseEntity.ok(top10Names);
    }
    
    @Override
    @PostMapping("/employees")
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody Employee employee) {
        EmployeeResponse created = employeeService.createEmployee(employee);
        if (created != null) {
            return ResponseEntity.ok(created);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        String deletedName = employeeService.deleteEmployeeById(id);
        if (deletedName != null) {
            return ResponseEntity.ok(deletedName);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
