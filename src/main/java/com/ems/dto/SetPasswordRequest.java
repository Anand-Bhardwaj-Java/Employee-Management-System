package com.ems.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetPasswordRequest {
	
	private String newPwd;
	
	private String confirmPwd;

}
