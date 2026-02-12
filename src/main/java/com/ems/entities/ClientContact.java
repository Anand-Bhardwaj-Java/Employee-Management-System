package com.ems.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ClientContact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer contactId; // Auto-generated Primary Key

	private String contactName;

	private String contactEmail;

	private String contactPhno;

	private String contactDesignation;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client; // Foreign key to Client table

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "uid")
	private UserEntity user;
}
