package com.example.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.entity.Employee;
import com.example.demo.entity.HR;
import com.example.demo.service.HRService;

@RestController
@RequestMapping("/hr")
public class HRController {

    @Autowired
    private HRService hrService;
    
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
	public ResponseEntity<String> registerEmployeeFromHR(@RequestBody Employee employee) {
		return hrService.registerEmployeeFromHR(employee);
	}
    
    @PutMapping("/update-employee")
    public ResponseEntity<String> updateEmployee(@RequestBody Employee employee) {
    	return hrService.updateEmployee(employee);
    }

    @GetMapping("/all-employee")
    public ResponseEntity<List<Employee>> getAllEmployees() {
       return hrService.getAllEmployees();
    }
    
    
    @DeleteMapping("/delete-employee/{eid}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long eid){
    	 return hrService.deleteEmployee(eid);
    }
}
