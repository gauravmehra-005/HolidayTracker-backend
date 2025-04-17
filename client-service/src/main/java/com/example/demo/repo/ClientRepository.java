package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findById(Long id);
}