package com.reliaquest.api.service;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeResponse;
import com.reliaquest.api.model.CreateEmployeeResponse;
import com.reliaquest.api.model.EmployeeApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private static final String EMPLOYEE_API_URL = "http://localhost:8112/api/v1/employee";

    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EmployeeService() {
        this(new RestTemplate());
    }

    public List<EmployeeResponse> getAllEmployees() {
        logger.info("Calling external mock API: {}", EMPLOYEE_API_URL);
        EmployeeApiResponse response = restTemplate.getForObject(EMPLOYEE_API_URL, EmployeeApiResponse.class);
        if (response != null && response.getData() != null) {
            logger.info("Received {} employees", response.getData().size());
            return response.getData();
        }
        logger.warn("No employees found or API response was null");
        return List.of();
    }

    public List<EmployeeResponse> getEmployeesByNameSearch(String nameFragment) {
        logger.info("Searching employees by name fragment: {}", nameFragment);
        List<EmployeeResponse> allEmployees = getAllEmployees();
        String lowerFragment = nameFragment.toLowerCase();
        List<EmployeeResponse> result = allEmployees.stream()
            .filter(e -> e.getEmployee_name() != null && e.getEmployee_name().toLowerCase().contains(lowerFragment))
            .toList();
        logger.info("Found {} employees matching '{}'", result.size(), nameFragment);
        return result;
    }

    public EmployeeResponse getEmployeeById(String id) {
        logger.info("Searching for employee with ID: {}", id);
        List<EmployeeResponse> allEmployees = getAllEmployees();
        EmployeeResponse found = allEmployees.stream()
                .filter(e -> e.getId() != null && e.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (found != null) {
            logger.info("Employee found: {}", found.getEmployee_name());
        } else {
            logger.warn("No employee found with ID: {}", id);
        }
        return found;
    }
    
    public int getHighestSalaryOfEmployees() {
        logger.info("Calculating highest salary among all employees");
        return getAllEmployees().stream()
                .mapToInt(EmployeeResponse::getEmployee_salary)
                .max()
                .orElse(0);
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        logger.info("Fetching top 10 highest earning employee names");
        return getAllEmployees().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getEmployee_salary(), e1.getEmployee_salary()))
                .limit(10)
                .map(EmployeeResponse::getEmployee_name)
                .toList();
    }

    public EmployeeResponse createEmployee(Employee employee) {
        logger.info("Creating employee: {}", employee.getName());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Employee> request = new HttpEntity<>(employee, headers);

        ResponseEntity<CreateEmployeeResponse> response = restTemplate.postForEntity(
            EMPLOYEE_API_URL, request, CreateEmployeeResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            EmployeeResponse created = response.getBody().getData();
            logger.info("Employee created successfully: {}", created != null ? created.getEmployee_name() : null);
            return created;
        }
        logger.warn("Failed to create employee: {}", employee.getName());
        return null;
    }

    public String deleteEmployeeById(String id) {
        logger.info("Deleting employee with ID: {}", id);

        EmployeeResponse employee = getEmployeeById(id);
        if (employee == null || employee.getEmployee_name() == null) {
            logger.warn("No employee found with ID: {}", id);
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> body = Map.of("name", employee.getEmployee_name());
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                EMPLOYEE_API_URL, HttpMethod.DELETE, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object data = response.getBody().get("data");
                boolean deleted = Boolean.TRUE.equals(data);
                if (deleted) {
                    logger.info("Deleted employee: {}", employee.getEmployee_name());
                    return employee.getEmployee_name();
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to delete employee with ID {}: {}", id, e.getMessage());
        }
        return null;
    }
}
