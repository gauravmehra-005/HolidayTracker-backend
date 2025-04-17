package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.HR;

public interface HRRepository extends JpaRepository<HR, Long> {
    Optional<HR> findByEmail(String email);
    Optional<HR> findById(Long id);
}