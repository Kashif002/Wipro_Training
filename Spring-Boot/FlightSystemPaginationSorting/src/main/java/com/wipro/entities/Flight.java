package com.wipro.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Flight {
	@Id
	private Integer flightNumber;
	private String flightName;
	private String arrival;
	private String departure;
	private String  journey;
}
