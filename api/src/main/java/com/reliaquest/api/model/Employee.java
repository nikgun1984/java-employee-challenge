package com.reliaquest.api.model;

public class Employee {
    private String id;
    private String name;
    private String title;
    private Integer salary;
    private Integer age;
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getSalary() { return salary; }
    public void setSalary(Integer salary) { this.salary = salary; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
