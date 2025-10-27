package com.reliaquest.api.model;

import java.util.List;

import com.reliaquest.api.model.EmployeeResponse;

public class EmployeeApiResponse {
    private List<EmployeeResponse> data;
    private String status;

    public List<EmployeeResponse> getData() { return data; }
    public void setData(List<EmployeeResponse> data) { this.data = data; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
