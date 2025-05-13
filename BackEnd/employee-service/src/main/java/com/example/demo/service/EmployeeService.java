package com.example.demo.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.Email;
import com.example.demo.entity.Employee;
import com.example.demo.repo.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
   
    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    KafkaTemplate<String,Email> kafkaTemplate;
    

//    private String emailServiceUrl="http://email-service/email/send";

    public void registerEmployee(Employee employee) {
        // Check if employee with same email already exists
        Employee existingEmployee = employeeRepository.findByEmail(employee.getEmail().toLowerCase()).orElse(null);
        if (existingEmployee != null) {
            throw new RuntimeException("Employee with email " + employee.getEmail() + " already exists");
        }
        employee.setEmail(employee.getEmail().toLowerCase());
        employee.setRole("EMPLOYEE");
        final String pwd=employee.getPassword();
	    employee.setPassword(passwordEncoder.encode((employee.getPassword())));
	    Employee e=employeeRepository.save(employee);
	    
	    Email mail=new Email();
	    mail.setTo(employee.getEmail());
	    mail.setSubject("Login Details for Holiday Portal");
	    mail.setBody("Hi! "+e.getName()+"\nWelcome to Wissen!\n"+"Your Login Credentials\n"+
	    		"Employee ID: "+e.getEid()+"\nPassword:"+pwd+"\nThanks & Regards\nHoliday Tracker Team");
	   
	    kafkaTemplate.send("login-creds-email",mail.getTo(),mail);
	    
//	    HttpEntity<Email> httpEntity=new HttpEntity<>(mail);
//      restTemplate.exchange(emailServiceUrl, HttpMethod.POST,httpEntity,String.class);
    }

    public Employee updateEmployee(Employee employee) {
        // Find existing employee by ID
        Employee existingEmployee = employeeRepository.findById(employee.getEid())
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        // Check if email is being changed and if new email already exists
        String newEmail = employee.getEmail().toLowerCase();
        if (!existingEmployee.getEmail().equals(newEmail)) {
            Employee emailCheck = employeeRepository.findByEmail(newEmail).orElse(null);
            if (emailCheck != null && !emailCheck.getEid().equals(employee.getEid())) {
                throw new RuntimeException("Employee with email " + employee.getEmail() + " already exists");
            }
        }

        employee.setEmail(newEmail);
        return employeeRepository.save(employee);
    }
    
    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email).orElse(null);
    }
    
    public List<Employee> getAllEmployeeUnderId(Long id){
    	return employeeRepository.findAllByHrId(id);
    }
    
    public String deleteEmployee(Employee e) {
    	if(employeeRepository.findByEmail(e.getEmail())==null)
    		return "Employee Not Found";
    	else {
    		employeeRepository.delete(e);
    		return "Employee Deleted Successfully";
    	}    		
    }
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }
}
