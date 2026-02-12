package com.ems.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.dto.EmployeeInfoResponse;
import com.ems.services.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService empService;

	@GetMapping("/info")
	public ResponseEntity<?> getEmployeeInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName(); // email

		EmployeeInfoResponse employeeInfo = empService.searchByIdOrEmail(email);
		if (employeeInfo != null) {
			return ResponseEntity.ok(employeeInfo);
			} else {
			return ResponseEntity.status(404).body("Employee not found.");
		}
	}
}
