package com.ems.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeInfoResponse {
	
	    private String empId;
	    
	    private String empName;
	    
	    private String empDept;
	    
	    private String empEmail;
	    
	    private Long empPhno;

	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	    private LocalDate joiningDate;

	    private String projectId;
	    
	    private String projectName;
	    
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	    private LocalDate projectEndDate;
	    
	    private String clientId;
	    
	    private String clientName;

}
