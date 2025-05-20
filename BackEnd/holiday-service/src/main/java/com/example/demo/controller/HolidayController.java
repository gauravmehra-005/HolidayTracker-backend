package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Holiday;
import com.example.demo.services.HolidayService;

@RestController
@RequestMapping("/holiday")
public class HolidayController {
	
	@Autowired
	HolidayService holidayService;
	
	@GetMapping("/all")
	public ResponseEntity<List<Holiday>> getAllHolidays(){
		return ResponseEntity.ok(holidayService.getHolidays());		
	}
	
	@PostMapping("/add")
	public ResponseEntity<Holiday> registerHoliday(@RequestBody Holiday holiday) {
		return ResponseEntity.ok(holidayService.registerHoliday(holiday));
	}
	
	
	@GetMapping("/holidayid")
    public ResponseEntity<Long> getHolidayId(@RequestParam String clientName, @RequestParam LocalDate holidayDate) {
        return ResponseEntity.ok(holidayService.getHolidayId(clientName, holidayDate));
    }
	
	@PutMapping("/update/{holidayId}")
	public ResponseEntity<Holiday> updateHoliday(@PathVariable Long holidayId, @RequestBody Holiday holiday) {
		return ResponseEntity.ok(holidayService.updateHoliday(holidayId,holiday));
	}
	
	@GetMapping("/get/{clientName}")
	public ResponseEntity<List<Holiday>> getHolidayWithClientName(@PathVariable String clientName ) {
		return ResponseEntity.ok(holidayService.getHolidayWithClientName(clientName));
	}
	
	@DeleteMapping("/delete/{holidayId}")
	public ResponseEntity<String> deleteHoliday(@PathVariable Long holidayId)
	{
		holidayService.deleteHoliday(holidayId);
		return ResponseEntity.ok("Deleted");
	}
	
	
}
