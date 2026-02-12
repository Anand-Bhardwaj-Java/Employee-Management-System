package com.ems.services;

import java.time.LocalDate;
import java.util.List;

import com.ems.dto.EmpUpdateRequest;
import com.ems.dto.EmployeeInfoResponse;
import com.ems.dto.EmployeeRequest;
import com.ems.dto.EmployeeResponse;

public interface EmployeeService {

	// add the Employee
	public String addEmployee(EmployeeRequest empRequest);

	// Getting all the Employee
	public List<EmployeeResponse> getEmployees();

	EmployeeInfoResponse searchByIdOrEmail(String value);

	// Get the Employee ByID
	public EmployeeInfoResponse getEmployeeById(String id);

	// Get the Employee Based On Email
	// public Employee getEmployeeByEmail(String email);

	// Get a list of employees who joined within a specified date range
	public List<EmployeeResponse> getEmployeesByDateRange(LocalDate startDate, LocalDate endDate);

	// Delete the Employee Based on EmployeeID
	public void deleteEmployee(String empId);

	// update the employee data except employee-id and employee-email.
	public EmployeeResponse updateEmployee(String empId, EmpUpdateRequest req);

	// Assign Employee to a Project
	public boolean assignProjectToEmployee(String empId, String projectId);

	// Release Employee from a Project (Set project_id to NULL)
	public boolean removeProjectFromEmployee(String empId);

	// Get Employees on the Bench (Without a Project)
	public List<EmployeeResponse> getEmployeesOnBench();

	// get all the employee on the basis of project-id
	public List<EmployeeResponse> getEmployeesByProjectId(String project_Id);

	// Checks if an employee is currently on the bench (not assigned to any project)
	public boolean isEmployeeOnBench(String employeeId);

	// set employee password
	public String setEmployeePwd(String empId, String pwd);

}
