package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SessionController {
	
	@GetMapping("/session")
	public ResponseEntity<String> getSessionInfo(){
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		if(auth==null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No active session");
		String username=auth.getName();
		String role=auth.getAuthorities().toString();
		return ResponseEntity.ok(username+" "+role);
	}

}
