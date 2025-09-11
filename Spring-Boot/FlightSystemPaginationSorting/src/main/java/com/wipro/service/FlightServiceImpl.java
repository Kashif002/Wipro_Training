package com.wipro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wipro.entities.Flight;
import com.wipro.repository.FlightRepository;

@Service
public class FlightServiceImpl implements FlightService{
	
	@Autowired
	FlightRepository flightRepository;
	
	@Override
	public Flight addFlightDetails(Flight flight) {
		Flight flightSaved =  flightRepository.save(flight);
		
		System.out.println("Flight details are saved");
		return flightSaved;
	}

	@Override
	public Flight updateFlightDetails(Integer flightNumber, Flight flight) {
		Flight flightUpdated = null;
		if(flightRepository.findById(flightNumber).get()!=null)
		{
			flightUpdated = flightRepository.save(flight);
		}
		else
		{
			flightUpdated = null;
		}
		return flightUpdated;
	}

	@Override
	public Flight getFlightDetials(Integer flightNumber) {
		return flightRepository.findById(flightNumber).get();
	}

	@Override
	public List<Flight> getAllFlightsDetails() {
		return flightRepository.findAll();
		}

	@Override
	public String deleteFlightDetails(Integer flightNumber) {
		Flight flightDelete = flightRepository.findById(flightNumber).get();
		String message ;
		if(flightDelete!=null)
		{
			flightRepository.delete(flightDelete);
			message = " flight details deleted successfully";
			
		}
		else
		{
			message = " flight details could not be deleted";
		}
		return message;
	}

	@Override
	public Page<Flight> getAllFlights(Pageable pageable) {
		// TODO Auto-generated method stub
		return flightRepository.findAll(pageable);
	}
}