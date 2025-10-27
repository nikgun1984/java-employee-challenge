package com.reliaquest.api.model;

public class CreateEmployeeResponse {
    private EmployeeResponse data;
    private String status;

    public EmployeeResponse getData() { return data; }
    public void setData(EmployeeResponse data) { this.data = data; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
