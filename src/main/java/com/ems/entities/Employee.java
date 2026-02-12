package com.ems.entities;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Employee {
	
	@Id
	private String empId;
	
	@Column(nullable = false)
	private String empName;
	
	@Column(nullable = false)
	private String empDept;
	
	@Column(nullable = false, unique = true)
	private String empEmail;
	
	@Column(nullable = false)
	private Long empPhno;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@CreationTimestamp
	private LocalDate joiningDate;
	
	@ManyToOne
	@JoinColumn(name = "project_id", nullable = true)
	private Project project;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "uid")
	private UserEntity user;
}
