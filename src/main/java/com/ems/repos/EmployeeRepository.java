package com.ems.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ems.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String>{

	Optional<Employee> findByEmpEmail(String empEmail);
	
	@Query("SELECT e FROM Employee e WHERE e.project IS NULL")
	List<Employee> findEmployeesOnBench();
	
}
