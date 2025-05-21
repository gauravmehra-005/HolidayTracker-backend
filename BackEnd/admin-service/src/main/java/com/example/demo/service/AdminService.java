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

import com.example.demo.entity.Admin;
import com.example.demo.entity.HR;
import com.example.demo.repo.AdminRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired 
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RestTemplate restTemplate;
    
    public Admin registerAdmin(Admin admin) {
        admin.setEmail(admin.getEmail().toLowerCase());
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }
    
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email).orElse(null);
    }
    
    public Admin getAdminProfile() {
    	Authentication auth=SecurityContextHolder.getContext().getAuthentication();
    	return getAdminByEmail(auth.getName());
    }
    
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }
    
    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
    
    public boolean deleteAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean deleteAdmin(Admin admin) {
        Admin existingAdmin = adminRepository.findByEmail(admin.getEmail()).orElse(null);
        if (existingAdmin == null) {
            return false;
        } else {
            adminRepository.delete(existingAdmin);
            return true;
        }
    }
    //Emp Management
    
    public ResponseEntity<?> getTotalHRs() {
    	String hrServiceUrl="http://hr-service/hr/count-hrs";
    	
    	HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String sessionId = request.getSession().getId();
        String encodedSessionId=Base64.getEncoder().encodeToString(sessionId.getBytes());
	    headers.add("Cookie", "SESSION=" + encodedSessionId + "; Path=/; HttpOnly");
    	HttpEntity<?> entity=new HttpEntity<>(headers);
    	ResponseEntity<Integer> response=restTemplate.exchange(hrServiceUrl,HttpMethod.GET,entity,Integer.class);
    	
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
    
    public ResponseEntity<?> getTotalClients() {
    	String clientServiceUrl="http://client-service/client/count-clients";
    	
    	HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String sessionId = request.getSession().getId();
        String encodedSessionId=Base64.getEncoder().encodeToString(sessionId.getBytes());
	    headers.add("Cookie", "SESSION=" + encodedSessionId + "; Path=/; HttpOnly");
    	HttpEntity<?> entity=new HttpEntity<>(headers);
    	ResponseEntity<Integer> response=restTemplate.exchange(clientServiceUrl,HttpMethod.GET,entity,Integer.class);
    	
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
    
    public ResponseEntity<?> getTotalEmployees() {
    	String clientServiceUrl="http://employee-service/employee/count-employees";
    	
    	HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String sessionId = request.getSession().getId();
        String encodedSessionId=Base64.getEncoder().encodeToString(sessionId.getBytes());
	    headers.add("Cookie", "SESSION=" + encodedSessionId + "; Path=/; HttpOnly");
    	HttpEntity<?> entity=new HttpEntity<>(headers);
    	ResponseEntity<Integer> response=restTemplate.exchange(clientServiceUrl,HttpMethod.GET,entity,Integer.class);
    	
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
    
    //HR Management
    
    public ResponseEntity<List<HR>> getAllHrs() {
    	String hrServiceUrl = "http://hr-service/hr/all-hrs";
        HttpHeaders headers=new HttpHeaders();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String sessionId = request.getSession().getId();
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
    
    public ResponseEntity<HR> getHrById(Long hrId) {
    	String hrServiceUrl = "http://hr-service/hr/hrs"+hrId;
        HttpHeaders headers=new HttpHeaders();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String sessionId = request.getSession().getId();
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
    
    public ResponseEntity<String> registerHRFromAdmin(HR hr) {
		String hrServiceUrl = "http://hr-service/hr/register-hr";
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    Admin admin = getAdminByEmail(auth.getName());
	    hr.setAdminId(admin.getAdminId());
	   
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String sessionId = request.getSession().getId();
        String encodedSessionId=Base64.getEncoder().encodeToString(sessionId.getBytes());
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
    
    public ResponseEntity<String> updateEmployee(HR hr) {
    	String hrServiceUrl = "http://hr-service/hr/update-hr";
    	Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Admin admin = getAdminByEmail(auth.getName());
        if (hr.getAdminId()!=admin.getAdminId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have authorization to update this hr");
        }
        try {
        	hr.setAdminId(admin.getAdminId());
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
	        String sessionId = request.getSession().getId();
	        String encodedSessionId=Base64.getEncoder().encodeToString(sessionId.getBytes());
			headers.add("Cookie", "SESSION=" + encodedSessionId + "; Path=/; HttpOnly");
			HttpEntity<?> httpEntity = new HttpEntity<>(hr, headers);
			ResponseEntity<String> response=restTemplate.exchange(hrServiceUrl,HttpMethod.PUT, httpEntity,String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
    
    
    public ResponseEntity<String> deleteHR(Long hrId){
   	 String hrServiceUrl = "http://hr-service/hr";
   	 hrServiceUrl+="/delete-hr/"+hrId;
        HttpHeaders headers=new HttpHeaders();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String sessionId = request.getSession().getId();
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
    
    public ResponseEntity<String> deleteHR(HR hr){
    	 String hrServiceUrl = "http://hr-service/hr/delete-hr";
         HttpHeaders headers=new HttpHeaders();
         RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
         HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
         String sessionId = request.getSession().getId();
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
