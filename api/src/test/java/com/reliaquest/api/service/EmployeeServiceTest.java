package com.reliaquest.api.service;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import org.springframework.http.HttpMethod;

import com.reliaquest.api.model.EmployeeResponse;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeApiResponse;
import com.reliaquest.api.model.CreateEmployeeResponse;

import com.reliaquest.api.model.EmployeeApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Test
    void getAllEmployees_returnsEmployees() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        EmployeeService service = new EmployeeService(restTemplate);

        EmployeeResponse emp1 = new EmployeeResponse();
        emp1.setId("id1");
        emp1.setEmployee_name("Alice");
        emp1.setEmployee_salary(50000);
        emp1.setEmployee_age(28);
        emp1.setEmployee_title("Developer");
        emp1.setEmployee_email("alice@company.com");

        EmployeeResponse emp2 = new EmployeeResponse();
        emp2.setId("id2");
        emp2.setEmployee_name("Bob");
        emp2.setEmployee_salary(60000);
        emp2.setEmployee_age(32);
        emp2.setEmployee_title("Manager");
        emp2.setEmployee_email("bob@company.com");

        EmployeeApiResponse mockApiResponse = new EmployeeApiResponse();
        mockApiResponse.setData(List.of(emp1, emp2));
        mockApiResponse.setStatus("success");

        when(restTemplate.getForObject(anyString(), eq(EmployeeApiResponse.class)))
            .thenReturn(mockApiResponse);

        List<EmployeeResponse> result = service.getAllEmployees();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getEmployee_name());
        assertEquals("Bob", result.get(1).getEmployee_name());
    }

    @Test
    void getEmployeesByNameSearch_returnsMatchingEmployees() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        EmployeeService service = new EmployeeService(restTemplate);

        EmployeeResponse emp1 = new EmployeeResponse();
        emp1.setId("1");
        emp1.setEmployee_name("Merlyn Dibbert");

        EmployeeResponse emp2 = new EmployeeResponse();
        emp2.setId("2");
        emp2.setEmployee_name("John Doe");

        EmployeeApiResponse mockResponse = new EmployeeApiResponse();
        mockResponse.setData(List.of(emp1, emp2));
        when(restTemplate.getForObject(anyString(), eq(EmployeeApiResponse.class))).thenReturn(mockResponse);

        List<EmployeeResponse> result = service.getEmployeesByNameSearch("Merlyn");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Merlyn Dibbert", result.get(0).getEmployee_name());
    }

    @Test
    void getEmployeeById_returnsCorrectEmployee() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        EmployeeService service = new EmployeeService(restTemplate);

        EmployeeResponse emp1 = new EmployeeResponse();
        emp1.setId("1");
        emp1.setEmployee_name("Merlyn Dibbert");

        EmployeeResponse emp2 = new EmployeeResponse();
        emp2.setId("2");
        emp2.setEmployee_name("John Doe");

        EmployeeApiResponse mockResponse = new EmployeeApiResponse();
        mockResponse.setData(List.of(emp1, emp2));
        when(restTemplate.getForObject(anyString(), eq(EmployeeApiResponse.class))).thenReturn(mockResponse);

        EmployeeResponse result = service.getEmployeeById("1");
        assertNotNull(result);
        assertEquals("Merlyn Dibbert", result.getEmployee_name());

        EmployeeResponse notFound = service.getEmployeeById("999");
        assertNull(notFound);
    }

    @Test
    void getHighestSalaryOfEmployees_returnsHighestSalary() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        EmployeeService service = new EmployeeService(restTemplate);

        EmployeeResponse emp1 = new EmployeeResponse();
        emp1.setEmployee_salary(50000);

        EmployeeResponse emp2 = new EmployeeResponse();
        emp2.setEmployee_salary(75000);

        EmployeeResponse emp3 = new EmployeeResponse();
        emp3.setEmployee_salary(60000);

        EmployeeApiResponse mockApiResponse = new EmployeeApiResponse();
        mockApiResponse.setData(List.of(emp1, emp2, emp3));
        mockApiResponse.setStatus("success");

        when(restTemplate.getForObject(anyString(), eq(EmployeeApiResponse.class)))
            .thenReturn(mockApiResponse);

        int highestSalary = service.getHighestSalaryOfEmployees();

        assertEquals(75000, highestSalary);
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_returnsTop10Names() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        EmployeeService service = new EmployeeService(restTemplate);

        // Prepare 12 employees with increasing salaries
        List<EmployeeResponse> employees = 
            java.util.stream.IntStream.range(0, 12)
                .mapToObj(i -> {
                    EmployeeResponse e = new EmployeeResponse();
                    e.setId("id" + i);
                    e.setEmployee_name("Employee" + i);
                    e.setEmployee_salary(1000 * i);
                    e.setEmployee_age(25 + i);
                    e.setEmployee_title("Title" + i);
                    e.setEmployee_email("emp" + i + "@company.com");
                    return e;
                })
                .toList();

        EmployeeApiResponse mockApiResponse = new EmployeeApiResponse();
        mockApiResponse.setData(employees);
        mockApiResponse.setStatus("success");

        when(restTemplate.getForObject(anyString(), eq(EmployeeApiResponse.class)))
            .thenReturn(mockApiResponse);

        List<String> top10Names = service.getTopTenHighestEarningEmployeeNames();

        assertEquals(10, top10Names.size());
        assertEquals("Employee11", top10Names.get(0)); // Highest salary
        assertEquals("Employee2", top10Names.get(9));  // 10th highest salary
    }

    @Test
    void createEmployee_returnsMappedEmployee() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        EmployeeService service = new EmployeeService(restTemplate);

        Employee input = new Employee();
        input.setName("Jane Doe");
        input.setTitle("Software Engineer");
        input.setSalary(60000);
        input.setAge(30);

        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setId("abc123");
        employeeResponse.setEmployee_name("Jane Doe");
        employeeResponse.setEmployee_title("Software Engineer");
        employeeResponse.setEmployee_salary(60000);
        employeeResponse.setEmployee_age(30);
        employeeResponse.setEmployee_email("jane.doe@company.com");

        CreateEmployeeResponse mockCreateResponse = new CreateEmployeeResponse();
        mockCreateResponse.setData(employeeResponse);
        mockCreateResponse.setStatus("success");

        ResponseEntity<CreateEmployeeResponse> mockResponse =
            new ResponseEntity<>(mockCreateResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(CreateEmployeeResponse.class)))
            .thenReturn(mockResponse);

        EmployeeResponse result = service.createEmployee(input);

        assertNotNull(result);
        assertNotNull(result.getId(), "ID should be generated and not null");
        assertEquals("Jane Doe", result.getEmployee_name());
        assertEquals("Software Engineer", result.getEmployee_title());
        assertEquals(60000, result.getEmployee_salary());
        assertEquals(30, result.getEmployee_age());
        assertEquals("jane.doe@company.com", result.getEmployee_email());
    }

    @Test
    void deleteEmployeeById_returnsEmployeeNameIfDeleted() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        EmployeeService service = spy(new EmployeeService(restTemplate));

        EmployeeResponse emp = new EmployeeResponse();
        emp.setId("id1");
        emp.setEmployee_name("Alice");

        doReturn(emp).when(service).getEmployeeById("id1");

        Map<String, Object> responseBody = Map.of("data", true, "status", "success");
        ResponseEntity<Map> mockResponse = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Map.class)))
            .thenReturn(mockResponse);

        String deletedName = service.deleteEmployeeById("id1");

        assertEquals("Alice", deletedName);
    }
}
