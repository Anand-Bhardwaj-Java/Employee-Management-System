package com.ems.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactResponse {
	
	private Integer contactId;

	private String contactName;

	private String contactEmail;

	private String contactPhno;

	private String contactDesignation;

}
