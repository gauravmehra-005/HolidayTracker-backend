package com.example.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    	return ResponseEntity.ok(adminService.getAdminProfile());
    }
    
    //DashBoard APIs
    
    @GetMapping("/dashboard/total-hrs")
    public ResponseEntity<?> getTotalHRs() {
    	return adminService.getTotalHRs();
    }
    
    @GetMapping("/dashboard/total-clients")
    public ResponseEntity<?> getTotalClients() {
    	return adminService.getTotalClients();
    }
    
    @GetMapping("/dashboard/total-employees")
    public ResponseEntity<?> getTotalEmployees() {
    	return adminService.getTotalEmployees();
    }
    
   // HR Management APIs
    
    @GetMapping("/all-hr")
    public ResponseEntity<List<HR>> getAllHrs() {
    	return adminService.getAllHrs();
    }

    @GetMapping("/hrs/{id}")
    public ResponseEntity<HR> getHrById(@PathVariable Long hrId) {
    	return adminService.getHrById(hrId);
    }
    
    @PostMapping("/register-hr")
	public ResponseEntity<String> registerHRFromAdmin(@RequestBody HR hr, HttpServletRequest request) {
		return adminService.registerHRFromAdmin(hr);
	}
    
    @PutMapping("/update-hr")
    public ResponseEntity<String> updateEmployee(@RequestBody HR hr) {
    	return adminService.updateEmployee(hr);
    }
    
    @DeleteMapping("/delete-hr/{hrId}")
    public ResponseEntity<String> deleteHR(@PathVariable Long hrId){
    	 return adminService.deleteHR(hrId);
    }
    
    @DeleteMapping("/delete-hr")
    public ResponseEntity<String> deleteHR(@RequestBody HR hr){
    	return adminService.deleteHR(hr);
    }
}