package com.ems.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.entities.Client;

public interface ClientRepository extends JpaRepository<Client, String> {

}
