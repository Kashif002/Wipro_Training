package com.wipro.dto;

import java.util.List;

public record Student_DTO(Long Id, String name , String city, List<String> emails)
{
		
}
