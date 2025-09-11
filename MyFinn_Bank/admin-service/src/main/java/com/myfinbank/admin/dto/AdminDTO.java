package com.myfinbank.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
    private Long adminId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;
    private Boolean isActive;
    private String lastLoginDate;
}
