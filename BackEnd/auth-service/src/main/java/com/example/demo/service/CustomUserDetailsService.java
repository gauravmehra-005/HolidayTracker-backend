package com.example.demo.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Admin;
import com.example.demo.entity.CustomUserDetails;
import com.example.demo.entity.Employee;
import com.example.demo.entity.HR;
import com.example.demo.repo.AdminRepository;
import com.example.demo.repo.EmployeeRepository;
import com.example.demo.repo.HRRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired private AdminRepository adminRepo;
    @Autowired private HRRepository hrRepo;
    @Autowired private EmployeeRepository empRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepo.findByEmail(username);
        if (admin.isPresent()) {
            return new CustomUserDetails(admin.get().getEmail(), admin.get().getPassword(), "ADMIN");
        }

        Optional<HR> hr = hrRepo.findByEmail(username);
        if (hr.isPresent()) {
            return new CustomUserDetails(hr.get().getEmail(), hr.get().getPassword(), "HR");
        }
        Optional<Employee> emp = empRepo.findByEmail(username);
        if (emp.isPresent()) {
            return new CustomUserDetails(emp.get().getEmail(), emp.get().getPassword(), "EMPLOYEE");
        }

        throw new UsernameNotFoundException("User not found");
    }
}
