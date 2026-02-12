package com.ems.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponse {
	
	private String empId;
	
	private String empName;
	
	private String empDept;
	
	private String empEmail;
	
	private Long empPhno;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate joiningDate;

}
