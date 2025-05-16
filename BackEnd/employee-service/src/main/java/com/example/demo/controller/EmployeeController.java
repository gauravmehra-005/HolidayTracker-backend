package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Employee;
import com.example.demo.entity.PasswordDTO;
import com.example.demo.service.EmployeeService;

import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    @GetMapping("/profile")
    public ResponseEntity<Employee> getEmployeeProfile() {
    	Authentication auth=SecurityContextHolder.getContext().getAuthentication();
    	Employee employee = employeeService.getEmployeeByEmail(auth.getName());
    	
    	if (employee == null) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    	}
    	
    	return ResponseEntity.ok(employee);
    }
    @GetMapping("/debug-session")
    public ResponseEntity<?> debugSession(HttpSession session, Authentication authentication) {
        return ResponseEntity.ok(Map.of(
            "sessionId", session.getId(),
            "authName", authentication.getName(),
            "authorities", authentication.getAuthorities()
        ));
    }
    
    @GetMapping("count-employees")
    public ResponseEntity<Integer> getClientCount(){
    	return ResponseEntity.ok(employeeService.getAllEmployees().size());
    }
    
    @PostMapping("/register-employee")
	  public ResponseEntity<String> registerEmployee(@RequestBody Employee employee) {
    	
	      employeeService.registerEmployee(employee);
	      return ResponseEntity.ok("Employee Registered Successfully");
	  }
	  
	  @PutMapping("/update-employee")
	  public ResponseEntity<String> updateEmployee(@RequestBody Employee e) {
	      try {
	          employeeService.updateEmployee(e);
	          return ResponseEntity.ok("Employee Updated Successfully");
	      } catch (RuntimeException ex) {
	          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	      }
	  }
	
	  
	  @GetMapping("/all-employee/{hrId}")
	  public ResponseEntity<List<Employee>> getAllEmployees(@PathVariable Long hrId) {
	      return ResponseEntity.ok(employeeService.getAllEmployeeUnderId(hrId));
	  }
	  
	  
	  @DeleteMapping("/delete-employee/{eid}")
	  public ResponseEntity<String> deleteEmployee(@PathVariable Long eid){
	      Employee e=employeeService.getEmployeeById(eid);
	      return ResponseEntity.ok(employeeService.deleteEmployee(e));
	  }
	  @PatchMapping("/change-password/{eid}")
	  public ResponseEntity<String> changePassword(@RequestBody PasswordDTO password, @PathVariable Long eid) {
	      boolean success = employeeService.changePassword(eid, password);

	      if (success) {
	          return ResponseEntity.ok("Password changed successfully");
	      } else {
	          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The password you provided was wrong");
	      }
	  }
}
