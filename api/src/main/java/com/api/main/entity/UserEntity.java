package com.api.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="SZS_USER")
public class UserEntity {
	
	@Id
	private String userId;

	@Column(length = 200)
	private String password;

	@Column(length = 200)
	private String name;

	@Column(length = 200)
	private String regNo;


}
