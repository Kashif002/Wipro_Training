package com.wipro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.entities.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {
	Page<Flight> findAll(Pageable pageable);
}
