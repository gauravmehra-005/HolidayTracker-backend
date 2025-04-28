package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Email;
import com.example.demo.service.EmailService;



@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    
    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody Email emailData){
    	try {
    		emailService.sendMail(emailData.getTo(), emailData.getSubject(), emailData.getBody());    
    		return ResponseEntity.ok("Mail successfully sent.");
    	}catch(Exception e)
    	{
    		return ResponseEntity.badRequest().body(e.getMessage());
    	}
    }
    
}