package com.ems.services;

import java.util.List;

import com.ems.dto.ProjectDto;
import com.ems.entities.Project;

public interface ProjectService {

	// add project
	public String addProject(String clientId, ProjectDto projectDto);

	// get All projects
	public List<Project> getAllProjects();

	// get project By project-id
	public Project getProjectById(String projectId);

	// delete project on the basis of project-id
	public void deleteProject(String projectId);

	// update project on the basis of project-id
	public Project updateProject(String projectId, ProjectDto projectDto);

	// get the project details from the client-id
	public List<Project> getProjectsByClientId(String clientId);

	// get the project details on the basis of employee-id.
	public Project getProjectByEmployeeId(String empId);

}
