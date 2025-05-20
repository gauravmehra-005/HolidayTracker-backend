package com.example.demo.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Holiday;
import com.example.demo.repos.HolidayRepository;

import jakarta.transaction.Transactional;

@Service
public class HolidayService {
	
	@Autowired
	private HolidayRepository holidayRepository;
	
	public Holiday registerHoliday(Holiday holiday) {
		return holidayRepository.save(holiday);
	}
	
	public List<Holiday> getHolidays(){
		return holidayRepository.findAll();
	}
	
    public Long getHolidayId(String clientName, LocalDate holidayDate) {
        Optional<Holiday> holiday = holidayRepository.findByClientNameAndHolidayDate(clientName, holidayDate);
        return holiday.map(Holiday::getHolidayId).orElse(null);
    }

    @Transactional
    public void deleteHoliday(Long holidayId) {
    	holidayRepository.deleteByHolidayId(holidayId);

    }
	
	public Holiday updateHoliday(Long holidayId, Holiday updatedHoliday) {
	    return holidayRepository.findById(holidayId)
	        .map(existingHoliday -> {
	        	existingHoliday.setName(updatedHoliday.getName());
	            existingHoliday.setHolidayDate(updatedHoliday.getHolidayDate());
	            existingHoliday.setDescription(updatedHoliday.getDescription());
	            return holidayRepository.save(existingHoliday); 
	        })
	        .orElseThrow(() -> new RuntimeException("Holiday with ID " + holidayId + " not found."));
	}
	
	public List<Holiday> getHolidayWithClientName(String clientName){
		return holidayRepository.findByClientName(clientName);
	}


}
