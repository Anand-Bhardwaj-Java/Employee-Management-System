package com.ems.controllers;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.dto.ContactResponse;
import com.ems.entities.Client;
import com.ems.entities.ClientContact;
import com.ems.entities.Project;
import com.ems.services.ClientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

	private final ClientService clientService;

	@GetMapping("/info")
	public ResponseEntity<Client> getClientInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return ResponseEntity.ok(clientService.getClientByEmail(email).getClient());
	}

	@GetMapping("/contacts")
	public ResponseEntity<?> getClientContacts() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		ClientContact contact = clientService.getClientByEmail(email);
		List<ContactResponse> list = contact.getClient().getClientContacts().stream()
				.map(c -> mapToContactResponse(c))
				.toList();

		if (list.isEmpty()) {
			return ResponseEntity.status(204).body("No Contacts found with client");
		}
		return ResponseEntity.ok(list);
	}

	@GetMapping("/projects")
	public ResponseEntity<?> getClientProjects() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		List<Project> projects = clientService.getClientByEmail(email).getClient().getProjects();

		if (projects.isEmpty()) {
			return ResponseEntity.status(204).body("No Project found with client");
		}
		return ResponseEntity.ok(projects);
	}

	private ContactResponse mapToContactResponse(ClientContact contact) {
		ContactResponse response = new ContactResponse();
		BeanUtils.copyProperties(contact, response);
		return response;
	}

}
