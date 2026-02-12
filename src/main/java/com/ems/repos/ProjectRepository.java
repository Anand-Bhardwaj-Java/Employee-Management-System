package com.ems.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ems.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, String>{
	
	@Query("SELECT p from Project p WHERE p.client.clientId = :clientId")
	public List<Project> findProjectsByClient(String clientId);

}
