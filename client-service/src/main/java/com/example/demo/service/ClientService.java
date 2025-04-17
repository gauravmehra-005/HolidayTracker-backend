package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Client;
import com.example.demo.repo.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client registerClient(Client client) {
        return clientRepository.save(client);
    }
    public List<Client> getClients()
    {
    	return clientRepository.findAll();
    }
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }
    public String deleteClient(Client e) {
    	if(clientRepository.findById(e.getClientId())==null)
    		return "Client Not Found";
    	else {
    		clientRepository.delete(e);
    		return "Client Deleted Successfully";
    	}    		
    }
}

