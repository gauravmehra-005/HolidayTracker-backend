package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> getSessionInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            Map<String, Object> response = new HashMap<>();
            response.put("isLoggedIn", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String username = auth.getName();
        String role = auth.getAuthorities().stream().findFirst().get().getAuthority(); 

        Map<String, Object> response = new HashMap<>();
        response.put("isLoggedIn", true);
        response.put("username", username);
        response.put("role", role);

        return ResponseEntity.ok(response);
    }
}
