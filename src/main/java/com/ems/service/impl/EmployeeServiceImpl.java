package com.ems.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ems.dto.EmpUpdateRequest;
import com.ems.dto.EmployeeInfoResponse;
import com.ems.dto.EmployeeRequest;
import com.ems.dto.EmployeeResponse;
import com.ems.entities.Client;
import com.ems.entities.Employee;
import com.ems.entities.Project;
import com.ems.entities.Role;
import com.ems.entities.UserEntity;
import com.ems.repos.EmployeeRepository;
import com.ems.repos.ProjectRepository;
import com.ems.services.EmployeeService;
import com.ems.utils.IdGeneratorUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository empRepo;

	private final ProjectRepository projectRepo;

	private final PasswordEncoder passwordEncoder;

	@Override
	public String addEmployee(EmployeeRequest empRequest) {
		if (empRepo.findByEmpEmail(empRequest.getEmpEmail()).isPresent()) {
			return "Email is already registered with Employee";
		}

		Employee emp = new Employee();
		BeanUtils.copyProperties(empRequest, emp);
		List<String> existingIds = empRepo.findAll().stream().map(Employee::getEmpId).toList(); // Fetch all current IDs
		String newId = IdGeneratorUtil.generateNextAvailableId(existingIds, "JTC-"); // Generate new ID
		emp.setEmpId(newId); // Set the generated ID
		return empRepo.save(emp).getEmpId() != null ? "Employee registered successfully!" : "Internal server error!";
	}

	@Override
	public List<EmployeeResponse> getEmployees() {
		return empRepo.findAll().stream().map(emp -> mapToEmployeeResponse(emp)).toList();
	}

	@Override
	public EmployeeInfoResponse searchByIdOrEmail(String value) {
		Optional<Employee> emp = empRepo.findById(value);
		if (emp.isPresent()) {
			return mapToEmployeeInfo(emp.get()) ;
		}
		return mapToEmployeeInfo(empRepo.findByEmpEmail(value).get()) ;
	}

	@Override
	public EmployeeInfoResponse getEmployeeById(String empId) {
		return mapToEmployeeInfo(empRepo.findById(empId).orElse(null)) ;
	}

	@Override
	public List<EmployeeResponse> getEmployeesByDateRange(LocalDate startDate, LocalDate endDate) {
		return empRepo.findAll().stream().filter(e -> e.getJoiningDate() != null
				&& !e.getJoiningDate().isBefore(startDate) && !e.getJoiningDate().isAfter(endDate))
				.map(emp -> mapToEmployeeResponse(emp)).toList();
	}

	@Override
	public void deleteEmployee(String empId) {
		empRepo.deleteById(empId);
	}

	@Override
	public EmployeeResponse updateEmployee(String empId, EmpUpdateRequest req) {
		Employee emp = empRepo.findById(empId).orElse(null);
		if (emp != null) {
			BeanUtils.copyProperties(req, emp);
			empRepo.save(emp);
			return mapToEmployeeResponse(emp);
		}
		return mapToEmployeeResponse(emp);
	}

	@Override
	public boolean assignProjectToEmployee(String empId, String projectId) {
		Optional<Employee> emp = empRepo.findById(empId);
		Optional<Project> project = projectRepo.findById(projectId);

		if (emp.isPresent() && project.isPresent()) {
			Employee employee = emp.get();
			employee.setProject(project.get());
			empRepo.save(employee);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeProjectFromEmployee(String empId) {
		return empRepo.findById(empId).map(emp -> {
			emp.setProject(null);
			empRepo.save(emp);
			return true;
		}).orElse(false);
	}

	@Override
	public List<EmployeeResponse> getEmployeesOnBench() {
		return empRepo.findEmployeesOnBench().stream().map(emp -> mapToEmployeeResponse(emp)).toList();
	}

	@Override
	public List<EmployeeResponse> getEmployeesByProjectId(String projectId) {
		return empRepo.findAll().stream()
				.filter(e -> e.getProject() != null && projectId.equals(e.getProject().getProjectId()))
				.map(emp -> mapToEmployeeResponse(emp)).toList();
	}

	@Override
	public boolean isEmployeeOnBench(String empId) {
		Optional<Employee> emp = empRepo.findById(empId);
		return emp.map(e -> e.getProject() == null).orElse(true);
	}

	@Override
	public String setEmployeePwd(String empId, String pwd) {
		Employee emp = empRepo.findById(empId).orElse(null);

		if (emp == null) {
			return "Employee not found. Invalid Employee ID!";
		}

		UserEntity user = emp.getUser();
		if (user == null) {
			user = new UserEntity();
		}

		user.setEmail(emp.getEmpEmail());
		user.setPassword(passwordEncoder.encode(pwd));
		user.setRole(Role.EMPLOYEE);

		emp.setUser(user);
		empRepo.save(emp);

		return "Employee Password Set Successfully!";
	}

	// Employee mapTo EmployeeRasponse
	private EmployeeResponse mapToEmployeeResponse(Employee emp) {
		if(emp != null) {
		EmployeeResponse response = new EmployeeResponse();
		BeanUtils.copyProperties(emp, response);
		return response;
		}
		return null;
	}

	private EmployeeInfoResponse mapToEmployeeInfo(Employee employee) {
		if(employee != null) {
		EmployeeInfoResponse info = new EmployeeInfoResponse();
		BeanUtils.copyProperties(employee, info);

		// Employee Project Info
		Project project = employee.getProject();
		if (project != null) {
			info.setProjectId(project.getProjectId());
			info.setProjectName(project.getProjectName());
			info.setProjectEndDate(project.getEndDate());

			// Employee client Info
			Client client = project.getClient();
			if (client != null) {
				info.setClientId(client.getClientId());
				info.setClientName(client.getClientName());
			}
		}
		return info;
		}
		return null;
	}

}
