package com.example.demo.entity;


import jakarta.persistence.*;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "EMPLOYEE";  

    @Column(nullable = false) 
    private String clientName;

    @Column(nullable = false)  
    private Long hrId;
    
    @Column(nullable = false)  
    private String department;	
    
    @Column(nullable = false)  
    private String designation; 
    
    @Column(nullable = false)  
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
