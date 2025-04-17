package com.example.demo.entity;


public class Employee {
    private Long eid;
    private String email;
    private String name;
    private String password;
    private String role;  
    private String clientName;
    private Long hrId;
    private String department;	
    private String designation; 
    private int age;
    
    public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Long getEid() { return eid; }
    public void setEid(Long eid) { this.eid = eid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    
    
    public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public Long getHrId() { return hrId; }
    public void setHrId(Long hrId) { this.hrId = hrId; }
}
