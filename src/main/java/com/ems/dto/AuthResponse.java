package com.ems.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {

	private String jwtToken;
	
	private String email;
}
