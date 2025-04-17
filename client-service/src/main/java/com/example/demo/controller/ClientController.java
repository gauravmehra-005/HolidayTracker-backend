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

import com.example.demo.entity.Client;
import com.example.demo.service.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    
    @GetMapping("/all")
    public List<Client> getClients()
    {
    	return clientService.getClients();
    }
    
    @GetMapping("count-clients")
    public ResponseEntity<Integer> getClientCount(){
    	return ResponseEntity.ok(clientService.getClients().size());
    }
    
  // Client Management APIs
    @GetMapping("/clients")
    public ResponseEntity<?> getAllClients() {
    	List<Client> clients = clientService.getClients();
    	return ResponseEntity.ok(clients);
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
    	Client client = clientService.getClientById(id);
    	if (client == null) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
    	}
    	return ResponseEntity.ok(client);
    }
    
    @PostMapping("/register-client")
    public Client registerClient(@RequestBody Client client) {
    	return clientService.registerClient(client);
    }
    
  
    @PutMapping("/update-client")
    public ResponseEntity<String> updateClient( @RequestBody Client e) {
    	clientService.registerClient(e);
    	return ResponseEntity.ok("Client Updated Successfully");
    }
  
    @DeleteMapping("/delete-client")
    public ResponseEntity<String> deleteClient(@RequestBody Client e){
      return ResponseEntity.ok(clientService.deleteClient(e));
    }
}