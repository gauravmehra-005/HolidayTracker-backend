package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Admin;
import com.example.demo.repo.AdminRepository;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired 
    private PasswordEncoder passwordEncoder;
    
    public Admin registerAdmin(Admin admin) {
        admin.setEmail(admin.getEmail().toLowerCase());
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }
    
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email).orElse(null);
    }
    
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }
    
    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
    
    public boolean deleteAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean deleteAdmin(Admin admin) {
        Admin existingAdmin = adminRepository.findByEmail(admin.getEmail()).orElse(null);
        if (existingAdmin == null) {
            return false;
        } else {
            adminRepository.delete(existingAdmin);
            return true;
        }
    }
}
