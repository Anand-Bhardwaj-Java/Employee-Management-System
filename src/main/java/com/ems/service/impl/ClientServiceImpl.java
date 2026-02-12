package com.ems.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ems.dto.ContactDto;
import com.ems.dto.ContactResponse;
import com.ems.dto.UpdateContactDto;
import com.ems.entities.Client;
import com.ems.entities.ClientContact;
import com.ems.entities.Project;
import com.ems.entities.Role;
import com.ems.entities.UserEntity;
import com.ems.repos.ClientContactRepository;
import com.ems.repos.ClientRepository;
import com.ems.repos.ProjectRepository;
import com.ems.services.ClientService;
import com.ems.utils.IdGeneratorUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepo;

	private final ClientContactRepository contactRepo;

	private final ProjectRepository projectRepo;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	public String addClient(String company) {
		Client client = new Client();
		client.setClientName(company);
		List<String> existingIds = clientRepo.findAll().stream().map(Client::getClientId).toList(); // Fetch all current IDs
		String newId = IdGeneratorUtil.generateNextAvailableId(existingIds, "CLIENT-"); // Generate new ID
		client.setClientId(newId); // Set the generated ID
		return clientRepo.save(client).getClientId() != null ? "Client added successfully!" : "Client not saved";
	}

	@Override
	public List<Client> getAllClients() {
		return clientRepo.findAll();
	}

	@Override
	public void deleteClient(String clientId) {
		Client client =clientRepo.findById(clientId).orElse(null);
		if(client != null) {
			clientRepo.deleteById(clientId);
		}
	}

	@Override
	public Client getClientByProjectId(String projectId) {
		Optional<Project> project = projectRepo.findById(projectId);
		return project.map(Project::getClient).orElse(null);
	}

	@Override
	public String addClientContact(String clientId,  ContactDto contact) {
		Client client = clientRepo.findById(clientId).orElse(null);
		if(client != null) {
			if (contactRepo.findByContactEmail(contact.getContactEmail()) != null) {
				return "Email is already present with contact!";
			}
			ClientContact clientContact = new ClientContact();
			BeanUtils.copyProperties(contact, clientContact);
			clientContact.setClient(client);
			return contactRepo.save(clientContact).getContactId() != null ? "Contact saved successfully!"
					: "Internal server error!";
		}
		return "Client not found. Invalid Client ID";
	}

	@Override
	public String updateClientContact(Integer contactId, UpdateContactDto contact) {
		Optional<ClientContact> existingContact = contactRepo.findById(contactId);
		if (existingContact.isPresent()) {
			ClientContact clientContact = existingContact.get();
			BeanUtils.copyProperties(contact, clientContact);
			contactRepo.save(clientContact);
			return "Contact updated successfully!";
		}
		return "Client contact not found!";
	}

	@Override
	public List<ContactResponse> getClientContactByClientId(String clientId) {
		return contactRepo.findByClientClientId(clientId).stream().map(contact -> mapToContactResponse(contact)).toList();
	}

	@Override
	public Client getClientById(String clientId) {
		return clientRepo.findById(clientId).orElse(null);
	}

	@Override
	public ClientContact getClientByEmail(String contactEmail) {
		return contactRepo.findByContactEmail(contactEmail) ;
	}

	@Override
	public void deleteContactById(int contactId) {
		contactRepo.deleteById(contactId);
	}

	@Override
	public ContactResponse getContactById(int contactId) {
		return mapToContactResponse(contactRepo.findById(contactId).get()) ;
	}

	@Override
	public String setClientPwd(int contactId, String pwd) {
		ClientContact contact = contactRepo.findById(contactId).orElse(null);
		if(contact == null) {
			return "Client Contact not found. Invalid Contact ID!";
		}
		
		UserEntity user = contact.getUser();
		if(user == null) {
			user = new UserEntity();
		}
		
		user.setEmail(contact.getContactEmail());
		user.setPassword(passwordEncoder.encode(pwd));
		user.setRole(Role.CLIENT);
		contact.setUser(user);
		contactRepo.save(contact);
		return "Client Password Set Successfully!";
	}
	
	// Convert ClientContact to ContactResponse
	private ContactResponse mapToContactResponse(ClientContact contact) {
		if(contact != null) {
		ContactResponse response = new ContactResponse();
		BeanUtils.copyProperties(contact, response);
		return response;
		}
		return null;
	}
}
