package com.myfinbank.admin.controller;

import com.myfinbank.admin.entity.Admin;
import com.myfinbank.admin.dto.AdminDTO;
import com.myfinbank.admin.service.AdminService;
import com.myfinbank.admin.config.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/admin")
@Tag(name = "Admin Management", description = "Admin authentication and profile management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    // Admin login page
    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    // Handle admin login
    @PostMapping("/login")
    @ResponseBody
    @Operation(summary = "Admin login", description = "Authenticate admin and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Bad request - Missing required fields"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> login(
            @Parameter(description = "Login credentials containing email and password", required = true)
            @RequestBody Map<String, String> loginRequest, 
            HttpServletResponse response) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            // Authenticate admin
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );

            // Generate JWT token
            String token = tokenProvider.generateToken(authentication);

            // Set JWT token as HTTP-only cookie for web navigation
            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);  // Prevent JavaScript access for security
            jwtCookie.setSecure(false);   // Set to true in production with HTTPS
            jwtCookie.setPath("/");      // Available for entire application
            jwtCookie.setMaxAge(24 * 60 * 60); // 24 hours (same as JWT expiration)
            response.addCookie(jwtCookie);
            
            // Also set adminToken cookie for backward compatibility
            Cookie adminTokenCookie = new Cookie("adminToken", token);
            adminTokenCookie.setHttpOnly(true);
            adminTokenCookie.setSecure(false);
            adminTokenCookie.setPath("/");
            adminTokenCookie.setMaxAge(24 * 60 * 60);
            response.addCookie(adminTokenCookie);

            // Update last login time
            adminService.updateLastLogin(email);

            // Return success response with token (for backward compatibility)
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Login successful");
            responseBody.put("token", token);  // Keep for AJAX calls
            responseBody.put("redirectUrl", "/admin/dashboard");

            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            // Return error response
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Invalid email or password");

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Admin registration page
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin/register";
    }

    // Handle admin registration
    @PostMapping("/register")
    @ResponseBody
    @Operation(summary = "Admin registration", description = "Register a new admin account")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Admin admin) {
        try {
            // Check if email already exists
            if (adminService.existsByEmail(admin.getEmail())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Email already exists");
                return ResponseEntity.badRequest().body(response);
            }

            // Register new admin
            AdminDTO registeredAdmin = adminService.registerAdmin(admin);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Admin registered successfully");
            response.put("admin", registeredAdmin);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get current admin profile
    @GetMapping("/profile")
    @ResponseBody
    @Operation(summary = "Get admin profile", description = "Get current admin profile information")
    public ResponseEntity<AdminDTO> getProfile() {
        // Get current authenticated admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Admin admin = adminService.findByEmail(email);
        AdminDTO adminDTO = convertToDTO(admin);

        return ResponseEntity.ok(adminDTO);
    }

    // Update admin profile
    @PutMapping("/profile")
    @ResponseBody
    @Operation(summary = "Update admin profile", description = "Update admin profile information")
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody Admin adminUpdate) {
        try {
            // Get current authenticated admin
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Admin currentAdmin = adminService.findByEmail(email);

            // Update admin profile (you'll need to implement this method in AdminService)
            AdminDTO updatedAdmin = adminService.updateAdmin(currentAdmin.getAdminId(), adminUpdate);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile updated successfully");
            response.put("admin", updatedAdmin);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Profile update failed: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    // Validate session endpoint for client-side checking
    @GetMapping("/validate-session")
    @ResponseBody
    @Operation(summary = "Validate session", description = "Validate admin session and return authentication status")
    public ResponseEntity<Map<String, Object>> validateSession(HttpServletRequest request) {
        try {
            // Check for JWT token in cookies
            boolean hasValidToken = false;
            String tokenValue = null;
            
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("jwt".equals(cookie.getName()) || "adminToken".equals(cookie.getName())) {
                        tokenValue = cookie.getValue();
                        if (tokenValue != null && !tokenValue.isEmpty() && !"null".equals(tokenValue)) {
                            // Validate the token
                            try {
                                if (tokenProvider.validateToken(tokenValue)) {
                                    hasValidToken = true;
                                    break;
                                }
                            } catch (Exception ex) {
                                // Token validation failed
                                System.out.println("Token validation failed: " + ex.getMessage());
                            }
                        }
                    }
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            if (hasValidToken) {
                response.put("valid", true);
                if (tokenValue != null) {
                    try {
                        String email = tokenProvider.getEmailFromToken(tokenValue);
                        response.put("username", email);
                    } catch (Exception e) {
                        // Ignore email extraction error
                    }
                }
                return ResponseEntity.ok(response);
            } else {
                response.put("valid", false);
                response.put("message", "No valid token found");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("message", "Session validation error: " + e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

    // Admin logout
    @PostMapping("/logout")
    @ResponseBody
    @Operation(summary = "Admin logout", description = "Logout admin and clear authentication tokens")
    public ResponseEntity<Map<String, Object>> logout(HttpServletResponse response) {
        // Clear security context
        SecurityContextHolder.clearContext();

        // Clear both JWT cookies (jwt and adminToken)
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);   // Set to true in production with HTTPS
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);       // Expire immediately
        response.addCookie(jwtCookie);
        
        Cookie adminTokenCookie = new Cookie("adminToken", null);
        adminTokenCookie.setHttpOnly(true);
        adminTokenCookie.setSecure(false);   // Set to true in production with HTTPS
        adminTokenCookie.setPath("/");
        adminTokenCookie.setMaxAge(0);       // Expire immediately
        response.addCookie(adminTokenCookie);

        // Clear JSESSIONID cookie
        Cookie sessionCookie = new Cookie("JSESSIONID", null);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "Logout successful");
        responseBody.put("redirectUrl", "/login");

        return ResponseEntity.ok(responseBody);
    }

    // Helper method to convert Admin entity to DTO
    private AdminDTO convertToDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.setAdminId(admin.getAdminId());
        dto.setEmail(admin.getEmail());
        dto.setFirstName(admin.getFirstName());
        dto.setLastName(admin.getLastName());
        dto.setPhoneNumber(admin.getPhoneNumber());
        dto.setRole(admin.getRole()); // Fixed: removed .toString() since role is now String
        dto.setIsActive(admin.getIsActive());
        return dto;
    }
}
