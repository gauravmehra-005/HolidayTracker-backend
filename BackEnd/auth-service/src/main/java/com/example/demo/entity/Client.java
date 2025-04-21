package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String spocName;  

    @Column(nullable = false)
    private String spocPno;   

    private int totalHolidays;

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSpocName() { return spocName; }
    public void setSpocName(String spocName) { this.spocName = spocName; }

    public String getSpocPno() { return spocPno; }
    public void setSpocPno(String spocPno) { this.spocPno = spocPno; }

    public int getTotalHolidays() { return totalHolidays; }
    public void setTotalHolidays(int totalHolidays) { this.totalHolidays = totalHolidays; }
}
