package com.ems.services;

import java.util.List;

import com.ems.dto.ContactDto;
import com.ems.dto.ContactResponse;
import com.ems.entities.Client;
import com.ems.entities.ClientContact;
import com.ems.dto.UpdateContactDto;

public interface ClientService {

	// add client
	public String addClient(String client);

	// get all clients
	public List<Client> getAllClients();

	// delete client by client-id
	public void deleteClient(String clientId);

	// get the clients information of the basis of project-id.
	public Client getClientByProjectId(String projectId);
	
	// save ClientContact
	public String addClientContact(String clientId, ContactDto contact);
	
	// update ClientContact
	public String updateClientContact(Integer contactId, UpdateContactDto contact);

	// Retrieve a list of client-contact for a given client ID.
	public List<ContactResponse> getClientContactByClientId(String clientId);

	// Retrieve client by client-id
	public Client getClientById(String clientId);

	 // Retrieve Client By Email
	 public ClientContact getClientByEmail(String contactEmail);

	public void deleteContactById(int contactId);
	
	public ContactResponse getContactById(int contactId);

	public String setClientPwd(int contactId, String confirmPwd);

}
