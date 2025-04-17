package com.example.demo.controller;


import java.util.List;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.Employee;
import com.example.demo.entity.HR;
import com.example.demo.service.HRService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/hr")
public class HRController {

    @Autowired
    private HRService hrService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private static final Logger logger = LoggerFactory.getLogger(HRController.class);
    
    @PostMapping("/register")
    public HR registerHR(@RequestBody HR hr) {
        return hrService.registerHR(hr);
    }

    
    @GetMapping("/count-hrs")
    public ResponseEntity<Integer> getHRCount(){
    	return ResponseEntity.ok(hrService.getAllHR().size());
    }
    
      // HR CRUD 
	  @GetMapping("/all-hrs")
	  public ResponseEntity<?> getAllHRs() {   
	      List<HR> hrs = hrService.getAllHR();
	      return ResponseEntity.ok(hrs);
	  }
	  
	  @GetMapping("/hrs/{id}")
	  public ResponseEntity<?> getHRById(@PathVariable Long id) {
	      HR hr = hrService.getHrById(id);
	      if (hr == null) {
	          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("HR not found");
	      }
	      return ResponseEntity.ok(hr);
	  }
	  
	  @PostMapping("/register-hr")
	  public ResponseEntity<?> addHR(@RequestBody HR hr) {
	      HR savedHR = hrService.registerHR(hr);
	      return ResponseEntity.status(HttpStatus.CREATED).body(savedHR);
	  }
	  
	  @PutMapping("/update-hr")
	  public ResponseEntity<String> updateEmployee(@RequestBody HR hr) {
	      hrService.registerHR(hr);
	      return ResponseEntity.ok("HR Updated Successfully");
	  }
	  
	  @DeleteMapping("/delete-hr/{hrId}")
	  public ResponseEntity<String> deleteHR(@PathVariable Long hrId){
		  hrService.deleteHRById(hrId);
	      return ResponseEntity.ok("Deleted Succesfully");
	  }
	  @DeleteMapping("/delete-hr")
	  public ResponseEntity<String> deleteHR(@RequestBody HR hr){
		  hrService.deleteHR(hr);
	      return ResponseEntity.ok("Deleted Succesfully");
	  }
	    
    //Employee CRUD Operations
    
	@PostMapping("/register-employee")
	public ResponseEntity<String> registerEmployeeFromHR(@RequestBody Employee employee, HttpServletRequest request) {
		String employeeServiceUrl = "http://employee-service/employee";
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    HR hr = hrService.getHRByEmail(auth.getName());
	    employee.setHrId(hr.getHrId());
	    employeeServiceUrl+="/register-employee";
	   
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    String sessionId = request.getSession().getId();
	    
	    // Base64 encode the session ID
	    String encodedSessionId = Base64.getEncoder().encodeToString(sessionId.getBytes());
	    logger.info("Base64 Encoded Session ID: {}", encodedSessionId);
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
    
    @PutMapping("/update-employee")
    public ResponseEntity<String> updateEmployee(@RequestBody Employee employee,HttpServletRequest request) {
    	String employeeServiceUrl = "http://employee-service/employee";
    	Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        HR hr = hrService.getHRByEmail(auth.getName());
        if (hr.getHrId()!=employee.getHrId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have authorization to update this employee");
        }
        try {
        	employee.setHrId(hr.getHrId());
			employeeServiceUrl+="/update-employee";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
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

    @GetMapping("/all-employee")
    public ResponseEntity<List<Employee>> getAllEmployees(HttpServletRequest request) {
       String employeeServiceUrl = "http://employee-service/employee";
    	Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        HR hr = hrService.getHRByEmail(auth.getName());
        employeeServiceUrl+="/all-employee/"+hr.getHrId();
        HttpHeaders headers=new HttpHeaders();
        String sessionId=request.getSession().getId();
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
    
    
    @DeleteMapping("/delete-employee/{eid}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long eid,HttpServletRequest request){
    	 	String employeeServiceUrl = "http://employee-service/employee";
    	 employeeServiceUrl+="/delete-employee/"+eid;
         HttpHeaders headers=new HttpHeaders();
         String sessionId=request.getSession().getId();
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
