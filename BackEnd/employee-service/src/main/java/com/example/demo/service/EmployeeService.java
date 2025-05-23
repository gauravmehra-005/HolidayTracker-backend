package com.example.demo.service;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.Email;
import com.example.demo.entity.Employee;
import com.example.demo.entity.PasswordDTO;
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
        // Check for existing email
        Employee existingEmployee = employeeRepository.findByEmail(employee.getEmail().toLowerCase()).orElse(null);
        if (existingEmployee != null) {
            throw new RuntimeException("Employee with email " + employee.getEmail() + " already exists");
        }

        employee.setEmail(employee.getEmail().toLowerCase());
        employee.setRole("EMPLOYEE");
        final String pwd = employee.getPassword();
        employee.setPassword(passwordEncoder.encode(pwd));
        Employee savedEmployee = employeeRepository.save(employee);

        Email mail = new Email();
        mail.setTo(employee.getEmail());
        mail.setSubject("Login Details for Holiday Portal");
        mail.setBody("Hi! " + savedEmployee.getName() + "\nWelcome to Wissen!\n" +
                     "Your Login Credentials\nEmployee ID: " + savedEmployee.getEid() +
                     "\nPassword: " + pwd + "\nThanks & Regards\nHoliday Tracker Team");

        try {
            kafkaTemplate.send("login-creds-email", mail.getTo(), mail).get(5, TimeUnit.SECONDS); // timeout
        } catch (Exception ex) {
            System.err.println("Kafka error: " + ex.getMessage());
        }
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
    public boolean changePassword(Long eid,PasswordDTO password) {
    	Employee e = getEmployeeById(eid);
    	if(passwordEncoder.matches(password.getOldPassword(),e.getPassword() )) {
    		e.setPassword(passwordEncoder.encode(password.getNewPassword()));
    		employeeRepository.save(e);
    		Email mail=new Email();
    	    mail.setTo(e.getEmail());
    	    mail.setSubject("Password Changed");
    	    mail.setBody(
    	    	    "Hi " + e.getName() + ",\n\n" +
    	    	    "We wanted to let you know that your password for your Holiday Tracker account (Employee ID: " + e.getEid() + ") was successfully changed.\n\n" +
    	    	    "If you did not initiate this change, please contact our support team immediately to secure your account.\n\n" +
    	    	    "Thank you for using Holiday Tracker.\n\n" +
    	    	    "Best regards,\n" +
    	    	    "The Holiday Tracker Team"
    	    	);

    	    kafkaTemplate.send("login-creds-email",mail.getTo(),mail);
    		return true;
    	}
    	return false;
    }
}
