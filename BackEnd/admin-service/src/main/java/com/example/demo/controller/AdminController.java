package com.example.demo.controller;


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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.Admin;
import com.example.demo.entity.HR;
import com.example.demo.service.AdminService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/admin")
public class AdminController {
	
    
    @Autowired
    private AdminService adminService;
    
    @Autowired RestTemplate restTemplate;
    
    @GetMapping("/profile")
    public ResponseEntity<Admin> getAdminProfile(){
    	Authentication auth=SecurityContextHolder.getContext().getAuthentication();
    	return ResponseEntity.ok(adminService.getAdminByEmail(auth.getName()));
    }
    
    //DashBoard APIs
    
    @GetMapping("/dashboard/total-hrs")
    public ResponseEntity<?> getTotalHRs(HttpServletRequest request) {
    	String hrServiceUrl="http://hr-service/hr/count-hrs";
    	
    	HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    String sessionId = request.getSession().getId();
	    String encodedSessionId = Base64.getEncoder().encodeToString(sessionId.getBytes());
	    headers.add("Cookie", "SESSION=" + encodedSessionId + "; Path=/; HttpOnly");
    	HttpEntity<?> entity=new HttpEntity<>(headers);
    	ResponseEntity<Integer> response=restTemplate.exchange(hrServiceUrl,HttpMethod.GET,entity,Integer.class);
    	
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
    
    @GetMapping("/dashboard/total-clients")
    public ResponseEntity<?> getTotalClients(HttpServletRequest request) {
    	String clientServiceUrl="http://client-service/client/count-clients";
    	
    	HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    String sessionId = request.getSession().getId();
	    String encodedSessionId = Base64.getEncoder().encodeToString(sessionId.getBytes());
	    headers.add("Cookie", "SESSION=" + encodedSessionId + "; Path=/; HttpOnly");
    	HttpEntity<?> entity=new HttpEntity<>(headers);
    	ResponseEntity<Integer> response=restTemplate.exchange(clientServiceUrl,HttpMethod.GET,entity,Integer.class);
    	
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
    
    @GetMapping("/dashboard/total-employees")
    public ResponseEntity<?> getTotalEmployees(HttpServletRequest request) {
    	String clientServiceUrl="http://employee-service/employee/count-employees";
    	
    	HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    String sessionId = request.getSession().getId();
	    String encodedSessionId = Base64.getEncoder().encodeToString(sessionId.getBytes());
	    headers.add("Cookie", "SESSION=" + encodedSessionId + "; Path=/; HttpOnly");
    	HttpEntity<?> entity=new HttpEntity<>(headers);
    	ResponseEntity<Integer> response=restTemplate.exchange(clientServiceUrl,HttpMethod.GET,entity,Integer.class);
    	
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

    }
    
   // HR Management APIs
    
    @GetMapping("/all-hr")
    public ResponseEntity<List<HR>> getAllHrs(HttpServletRequest request) {
    	String hrServiceUrl = "http://hr-service/hr/all-hrs";
        HttpHeaders headers=new HttpHeaders();
        String sessionId=request.getSession().getId();
        String encodedSessionId=Base64.getEncoder().encodeToString(sessionId.getBytes());
        headers.add("Cookie", "SESSION="+encodedSessionId+"; Path=/; HttpOnly");
        HttpEntity<?> entity=new HttpEntity<>(headers);
        ResponseEntity<List<HR>> response = restTemplate.exchange(
        	    hrServiceUrl,
        	    HttpMethod.GET,
        	    entity,
        	    new ParameterizedTypeReference<List<HR>>() {}
        	);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @GetMapping("/hrs/{id}")
    public ResponseEntity<HR> getHrById(@PathVariable Long hrId,HttpServletRequest request) {
    	String hrServiceUrl = "http://hr-service/hr/hrs"+hrId;
        HttpHeaders headers=new HttpHeaders();
        String sessionId=request.getSession().getId();
        String encodedSessionId=Base64.getEncoder().encodeToString(sessionId.getBytes());
        headers.add("Cookie", "SESSION="+encodedSessionId+"; Path=/; HttpOnly");
        HttpEntity<?> entity=new HttpEntity<>(headers);
        ResponseEntity<HR> response = restTemplate.exchange(
        	    hrServiceUrl,
        	    HttpMethod.GET,
        	    entity,
        	    HR.class
        	);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
    
    @PostMapping("/register-hr")
	public ResponseEntity<String> registerHRFromAdmin(@RequestBody HR hr, HttpServletRequest request) {
		String hrServiceUrl = "http://hr-service/hr/register-hr";
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    Admin admin = adminService.getAdminByEmail(auth.getName());
	    hr.setAdminId(admin.getAdminId());
	   
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    String sessionId = request.getSession().getId();
	    String encodedSessionId = Base64.getEncoder().encodeToString(sessionId.getBytes());
	    headers.add("Cookie", "SESSION=" + encodedSessionId + "; Path=/; HttpOnly");
	    HttpEntity<?> httpEntity = new HttpEntity<>(hr, headers);
	
	    try {
	        ResponseEntity<String> response = restTemplate.exchange(
	            hrServiceUrl,
	            HttpMethod.POST,
	            httpEntity,
	            String.class
	        );
	        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
	    } catch (HttpStatusCodeException ex) {
	        return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error communicating with HR Service: " + e.getMessage());
	    }
	}
    
    @PutMapping("/update-hr")
    public ResponseEntity<String> updateEmployee(@RequestBody HR hr,HttpServletRequest request) {
    	String hrServiceUrl = "http://hr-service/hr/update-hr";
    	Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Admin admin = adminService.getAdminByEmail(auth.getName());
        if (hr.getAdminId()!=admin.getAdminId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have authorization to update this hr");
        }
        try {
        	hr.setAdminId(admin.getAdminId());
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			String sessionId = request.getSession().getId();
			String encodedSessionId = Base64.getEncoder().encodeToString(sessionId.getBytes());
			headers.add("Cookie", "SESSION=" + encodedSessionId + "; Path=/; HttpOnly");
			HttpEntity<?> httpEntity = new HttpEntity<>(hr, headers);
			ResponseEntity<String> response=restTemplate.exchange(hrServiceUrl,HttpMethod.PUT, httpEntity,String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
    
    @DeleteMapping("/delete-hr/{hrId}")
    public ResponseEntity<String> deleteHR(@PathVariable Long hrId,HttpServletRequest request){
    	 String hrServiceUrl = "http://hr-service/hr";
    	 hrServiceUrl+="/delete-hr/"+hrId;
         HttpHeaders headers=new HttpHeaders();
         String sessionId=request.getSession().getId();
         String encodedSessionId=Base64.getEncoder().encodeToString(sessionId.getBytes());
         headers.add("Cookie", "SESSION="+encodedSessionId+"; Path=/; HttpOnly");
         HttpEntity<?> entity=new HttpEntity<>(headers);
         ResponseEntity<String> response = restTemplate.exchange(
         	    hrServiceUrl,
         	    HttpMethod.DELETE,
         	    entity,
         	    String.class
         	);

         return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
    
    @DeleteMapping("/delete-hr")
    public ResponseEntity<String> deleteHR(@RequestBody HR hr,HttpServletRequest request){
    	 String hrServiceUrl = "http://hr-service/hr/delete-hr";
         HttpHeaders headers=new HttpHeaders();
         String sessionId=request.getSession().getId();
         String encodedSessionId=Base64.getEncoder().encodeToString(sessionId.getBytes());
         headers.add("Cookie", "SESSION="+encodedSessionId+"; Path=/; HttpOnly");
         HttpEntity<?> entity=new HttpEntity<>(hr,headers);
         ResponseEntity<String> response = restTemplate.exchange(
         	    hrServiceUrl,
         	    HttpMethod.DELETE,
         	    entity,
         	    String.class
         	);

         return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}