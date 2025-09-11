package com.wipro.Validation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.Validation.dao.UserRepository;
import com.wipro.Validation.model.User;

@Service
public class UserService {
	
	// Firstly autowired the UserRepository
	@Autowired
	public UserRepository userRepo;
	
	//UserRepository userRepo;
	
	public User saveUSer(User u)
	{
		return userRepo.save(u);
	}
	
	public List<User> getAllUsers()
	{
		return userRepo.findAll();
	}

}