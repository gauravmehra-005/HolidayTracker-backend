package com.example.demo.service;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.entity.Employee;
import com.example.demo.entity.HR;
import com.example.demo.repo.HRRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class HRService {

    @Autowired
    private HRRepository hrRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    RestTemplate restTemplate;
  
    public HR registerHR(HR hr) {
    	hr.setEmail(hr.getEmail().toLowerCase());
    	hr.setPassword(passwordEncoder.encode(hr.getPassword()));
        return hrRepository.save(hr);
    }
    

    public String deleteHR(HR e) {
    	if(hrRepository.findByEmail(e.getEmail())==null)
    		return "HR Not Found";
    	else {
    		hrRepository.delete(e);
    		return "HR Deleted Successfully";
    	}    		
    }
    
    public void deleteHRById(Long id)
    {
    	hrRepository.deleteById(id);
    }
    public HR getHRByEmail(String email) {
    	return hrRepository.findByEmail(email).orElse(null);
    }
    
    public List<HR> getAllHR(){
    	return hrRepository.findAll();
    }
    
    public HR getHrById(Long id) {
        return hrRepository.findById(id).orElse(null);
    }
    
    //Employee CRUD
	public ResponseEntity<String> registerEmployeeFromHR( Employee employee) {
		String employeeServiceUrl = "http://employee-service/employee";
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    HR hr = getHRByEmail(auth.getName());
	    employee.setHrId(hr.getHrId());
	    employeeServiceUrl+="/register-employee";
	   
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
	    String sessionId = request.getSession().getId();
	    
	    // Base64 encode the session ID
	    String encodedSessionId = Base64.getEncoder().encodeToString(sessionId.getBytes());
	    headers.add("Cookie", "SESSION=" + encodedSessionId + "; Path=/; HttpOnly");
	    HttpEntity<Employee> httpEntity = new HttpEntity<>(employee, headers);
	
	    try {
	        ResponseEntity<String> response = restTemplate.exchange(
	            employeeServiceUrl,
	            HttpMethod.POST,
	            httpEntity,
	            String.class
	        );
	        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
	    } catch (HttpStatusCodeException ex) {
	        return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error communicating with Employee Service: " + e.getMessage());
	    }
	}
    
    public ResponseEntity<List<Employee>> getAllEmployees() {
        String employeeServiceUrl = "http://employee-service/employee";
     	Authentication auth=SecurityContextHolder.getContext().getAuthentication();
         HR hr = getHRByEmail(auth.getName());
         employeeServiceUrl+="/all-employee/"+hr.getHrId();
         HttpHeaders headers=new HttpHeaders();
         RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
         HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
         String sessionId = request.getSession().getId();
         String encodedSessionId=Base64.getEncoder().encodeToString(sessionId.getBytes());
         headers.add("Cookie", "SESSION="+encodedSessionId+"; Path=/; HttpOnly");
         HttpEntity<Employee> entity=new HttpEntity<>(headers);
         ResponseEntity<List<Employee>> response = restTemplate.exchange(
         	    employeeServiceUrl,
         	    HttpMethod.GET,
         	    entity,
         	    new ParameterizedTypeReference<List<Employee>>() {}
         	);

         return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
     }
    
    public ResponseEntity<String> updateEmployee(Employee employee) {
    	String employeeServiceUrl = "http://employee-service/employee";
    	Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        HR hr = getHRByEmail(auth.getName());
        if (hr.getHrId()!=employee.getHrId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have authorization to update this employee");
        }
        try {
        	employee.setHrId(hr.getHrId());
			employeeServiceUrl+="/update-employee";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			 RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			 HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			 String sessionId = request.getSession().getId();
			    // Base64 encode the session ID
			String encodedSessionId = Base64.getEncoder().encodeToString(sessionId.getBytes());
			headers.add("Cookie", "SESSION=" + encodedSessionId + "; Path=/; HttpOnly");
			HttpEntity<Employee> httpEntity = new HttpEntity<>(employee, headers);
			ResponseEntity<String> response=restTemplate.exchange(employeeServiceUrl,HttpMethod.PUT, httpEntity,String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
    
    public ResponseEntity<String> deleteEmployee(Long eid){
	 String employeeServiceUrl = "http://employee-service/employee";
	 employeeServiceUrl+="/delete-employee/"+eid;
     HttpHeaders headers=new HttpHeaders();
     RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
     HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
     String sessionId = request.getSession().getId();
     String encodedSessionId=Base64.getEncoder().encodeToString(sessionId.getBytes());
     headers.add("Cookie", "SESSION="+encodedSessionId+"; Path=/; HttpOnly");
     HttpEntity<Employee> entity=new HttpEntity<>(headers);
     ResponseEntity<String> response = restTemplate.exchange(
     	    employeeServiceUrl,
     	    HttpMethod.DELETE,
     	    entity,
     	    String.class
     	);

     return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
}
}
