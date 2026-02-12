package com.ems.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpUpdateRequest {

	private String empName;

	private String empDept;

	private Long empPhno;
}
