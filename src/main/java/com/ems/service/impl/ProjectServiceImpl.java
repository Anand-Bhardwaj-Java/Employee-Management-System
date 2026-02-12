package com.ems.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.ems.dto.ProjectDto;
import com.ems.entities.Client;
import com.ems.entities.Employee;
import com.ems.entities.Project;
import com.ems.repos.ClientRepository;
import com.ems.repos.EmployeeRepository;
import com.ems.repos.ProjectRepository;
import com.ems.services.ProjectService;
import com.ems.utils.IdGeneratorUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepo;

	private final ClientRepository clientRepo;

	private final EmployeeRepository empRepo;

	@Override
	public String addProject(String clientId, ProjectDto projectDto) {
		Client client = clientRepo.findById(clientId).orElse(null);

		if (client != null) {
			Project project = new Project();
			BeanUtils.copyProperties(projectDto, project);
			project.setClient(client);
			List<String> existingIds = projectRepo.findAll().stream().map(Project::getProjectId).toList(); // Fetch all current IDs
			String newId = IdGeneratorUtil.generateNextAvailableId(existingIds, "PROJECT-"); // Generate new ID
			project.setProjectId(newId); // Set the generated ID
			projectRepo.save(project);
			return "Project added successfully!";
		}
		return "Client not found. Invalid client ID!";
	}

	@Override
	public List<Project> getAllProjects() {
		return projectRepo.findAll();
	}

	@Override
	public Project getProjectById(String projectId) {
		return projectRepo.findById(projectId).orElse(null);
	}

	@Override
	public void deleteProject(String projectId) {
		Project project = projectRepo.findById(projectId).orElse(null);
		if(project != null) {
			project.getEmployees().forEach(e -> e.setProject(null));
			projectRepo.deleteById(projectId);
		}
		
	}

	@Override
	public Project updateProject(String projectId, ProjectDto projectDto) {
		Project exstingProject = projectRepo.findById(projectId).orElse(null);
		if (exstingProject != null) {
			BeanUtils.copyProperties(projectDto, exstingProject);
			return projectRepo.save(exstingProject);
		}
		return null;
	}

	@Override
	public List<Project> getProjectsByClientId(String clientId) {
		return projectRepo.findProjectsByClient(clientId);
	}

	@Override
	public Project getProjectByEmployeeId(String empId) {
		return empRepo.findById(empId)
				.map(Employee::getProject)
				.orElse(null);
	}
}
