package com.ems.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.entities.ClientContact;
import java.util.List;

public interface ClientContactRepository extends JpaRepository<ClientContact, Integer> {

	ClientContact findByContactEmail(String contactEmail);

	List<ClientContact> findByClientClientId(String clientId);

}
