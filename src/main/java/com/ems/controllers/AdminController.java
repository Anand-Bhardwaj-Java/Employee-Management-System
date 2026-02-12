package com.ems.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ems.dto.ContactDto;
import com.ems.dto.ContactResponse;
import com.ems.dto.EmpUpdateRequest;
import com.ems.dto.EmployeeInfoResponse;
import com.ems.dto.EmployeeRequest;
import com.ems.dto.EmployeeResponse;
import com.ems.dto.ProjectDto;
import com.ems.dto.SetPasswordRequest;
import com.ems.dto.UpdateContactDto;
import com.ems.entities.Client;
import com.ems.entities.Project;
import com.ems.services.ClientService;
import com.ems.services.EmployeeService;
import com.ems.services.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final EmployeeService empService;

	private final ClientService clientService;

	private final ProjectService projectService;

//  <====================> Manage Employee Operations <====================>

	@PostMapping("/create-employee")
	public ResponseEntity<String> createEmployee(@RequestBody EmployeeRequest empRequest) {
		String status = empService.addEmployee(empRequest);
		return new ResponseEntity<>(status,
				status.equals("Employee registered successfully!") ? HttpStatus.CREATED : HttpStatus.CONFLICT);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/employees")
	public List<EmployeeResponse> getAllEmployees() {
		return empService.getEmployees();
	}

	@GetMapping("/search-employee")
	public ResponseEntity<?> searchByIdOrEmail(@RequestParam String value) {
		EmployeeInfoResponse employee = empService.searchByIdOrEmail(value);

		if (employee != null) {
			return ResponseEntity.ok(employee);
		}
		return ResponseEntity.badRequest().body("Invalid email or Employee ID");
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/delete-employee/{empId}")
	public void deleteEmployee(@PathVariable String empId) {
		empService.deleteEmployee(empId);
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/update-employee/{empId}")
	public EmployeeResponse updateEmployee(@PathVariable String empId, @RequestBody EmpUpdateRequest req) {
		return empService.updateEmployee(empId, req);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/employees/date-range")
	public List<EmployeeResponse> getEmployeesByDateRange(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
		return empService.getEmployeesByDateRange(startDate, endDate);
	}

	@PutMapping("/assign-project")
	public ResponseEntity<String> assignProjectToEmployee(@RequestParam String empId, @RequestParam String projectId) {
		boolean status = empService.assignProjectToEmployee(empId, projectId);

		if (status) {
			return ResponseEntity.ok("Project assigned to employee successfully");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee or Project not found");
		}
	}

	@PutMapping("/remove-project/{empId}")
	public ResponseEntity<String> removeProjectFromEmployee(@PathVariable String empId) {
		boolean status = empService.removeProjectFromEmployee(empId);
		return status ? ResponseEntity.ok("Project removed from employee successfully.")
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found. Invalid employee ID!");
	}

	@GetMapping("/employees/on-bench")
	public ResponseEntity<List<EmployeeResponse>> getEmployeesOnBench() {
		List<EmployeeResponse> benchEmployees = empService.getEmployeesOnBench();

		if (benchEmployees.isEmpty()) {
			return ResponseEntity.noContent().build(); // 204 No Content
		}
		return ResponseEntity.ok(benchEmployees); // 200 OK with list
	}

	@GetMapping("/employees/by-project")
	public ResponseEntity<List<EmployeeResponse>> getEmployeesByProject(@RequestParam String projectId) {
		List<EmployeeResponse> employees = empService.getEmployeesByProjectId(projectId);

		if (employees.isEmpty()) {
			return ResponseEntity.noContent().build(); // 204 No Content
		}
		return ResponseEntity.ok(employees); // 200 OK
	}
	
	@PatchMapping("set-employee-pwd/{empId}")
	public ResponseEntity<String> setEmployeePwd(@PathVariable String empId, @RequestBody SetPasswordRequest request) {

		if(!request.getNewPwd().equals(request.getConfirmPwd())) {
		return ResponseEntity.badRequest().body("newPwd and confirmPwd does not match!");
		}
		String status = empService.setEmployeePwd(empId, request.getConfirmPwd());
		return new ResponseEntity<>(status, status.equals( "Employee Password Set Successfully!") ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}

//  <====================> Manage Client Operations <====================>

	@PostMapping("/add-client")
	public ResponseEntity<String> addClient(String companyName) {
		String status = clientService.addClient(companyName);
		return new ResponseEntity<>(status,
				status.equals("Client added successfully!") ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/clients")
	public List<Client> getAllClients() {
		return clientService.getAllClients();
	}

	@GetMapping("/client-by-id")
	public ResponseEntity<?> getClientById(@PathVariable String clientId) {
		Client client = clientService.getClientById(clientId);
		if (client != null) {
			return ResponseEntity.ok(client);
		}
		return ResponseEntity.badRequest().body("Client not found. Invalid Client ID!");
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/delete-client/{clientId}")
	public void deleteClient(@PathVariable String clientId) {
		clientService.deleteClient(clientId);
	}

	@GetMapping("/client/by-project")
	public ResponseEntity<?> getClientByProjectId(@RequestParam String projectId) {
		Client client = clientService.getClientByProjectId(projectId);
		if (client != null) {
			return ResponseEntity.ok(client);
		}
		return ResponseEntity.badRequest().body("Client not found. Invalid project ID!");
	}

	@PostMapping("/add-client-contact/{clientId}")
	public ResponseEntity<String> addClientContact(@PathVariable String clientId, @RequestBody ContactDto contactDto) {
		String status = clientService.addClientContact(clientId, contactDto);
		return new ResponseEntity<>(status,
				status.equals("Contact saved successfully!") ? HttpStatus.CREATED : HttpStatus.CONFLICT);

	}

	@PutMapping("/update-client-contact/{contactId}")
	public ResponseEntity<String> updateClientContact(@PathVariable Integer contactId,
			@RequestBody UpdateContactDto contactDto) {
		String status = clientService.updateClientContact(contactId, contactDto);
		return new ResponseEntity<>(status,
				status.equals("Contact updated successfully!") ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}

	@GetMapping("/client-contacts")
	public ResponseEntity<?> getClientContactByClientId(@RequestParam String clientId) {
		List<ContactResponse> contactList = clientService.getClientContactByClientId(clientId);

		if (!contactList.isEmpty()) {
			return ResponseEntity.ok(contactList);
		}
		return ResponseEntity.noContent().build();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/delete-contact/{contactId}")
	public void deleteContactById(@PathVariable int contactId) {
		clientService.deleteContactById(contactId);
	}
	
	@PatchMapping("set-client-pwd/{contactId}")
	public ResponseEntity<String> setClientPwd(@PathVariable int contactId, @RequestBody SetPasswordRequest request) {
		
		if(!request.getNewPwd().equals(request.getConfirmPwd())) {
			return ResponseEntity.badRequest().body("newPwd and confirmPwd does not match!");
		}
		String status = clientService.setClientPwd(contactId, request.getConfirmPwd());
		return new ResponseEntity<>(status, status.equals( "Client Password Set Successfully!") ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}

//  <====================> Manage Project Operations <====================>

	@PostMapping("/add-project/{clientId}")
	public ResponseEntity<String> addProject(@PathVariable String clientId, @RequestBody ProjectDto projectDto) {
		String status = projectService.addProject(clientId, projectDto);
		return new ResponseEntity<>(status,
				status.equals("Project added successfully!") ? HttpStatus.CREATED : HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/projects")
	public List<Project> getAllProjects() {
		return projectService.getAllProjects();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/delete-project/{projectId}")
	public void deleteProject(@PathVariable String projectId) {
		projectService.deleteProject(projectId);
	}

	@PutMapping("/update-project/{projectId}")
	public ResponseEntity<Project> updateProject(@PathVariable String projectId, @RequestBody ProjectDto projectDto) {
		Project updateProject = projectService.updateProject(projectId, projectDto);
		return new ResponseEntity<>(updateProject, updateProject != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/projects/by-client")
	public ResponseEntity<?> getProjectsByClientId(@RequestParam String clientId) {
		List<Project> projectList = projectService.getProjectsByClientId(clientId);
		if (projectList.isEmpty()) {
			return ResponseEntity.badRequest().body("Projects are not found or Invalid Client ID!");	
		}
		return ResponseEntity.ok(projectList);
	}

	@GetMapping("/project/by-employee")
	public ResponseEntity<Project> getProjectByEmployeeId(@RequestParam String empId) {
		Project project = projectService.getProjectByEmployeeId(empId);
		return new ResponseEntity<>(project, project != null ? HttpStatus.OK : HttpStatus.NO_CONTENT);
	}

}
