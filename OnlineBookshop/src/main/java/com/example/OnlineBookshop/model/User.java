package com.example.OnlineBookshop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    public Long getUserId() 
    { 
    	return userId; 
    }
    
    public void setUserId(Long userId) 
    { 
    	this.userId = userId; 
    }
    
    public String getUsername() 
    { 
    	return username; 
    }
    
    public void setUsername(String username) 
    { 
    	this.username = username; 
    }
    
    public String getPassword() 
    { 
    	return password; 
    }
    
    public void setPassword(String password) 
    { 
    	this.password = password; 
    }
    
    public String getEmail() 
    { 
    	return email;
    }
    
    public void setEmail(String email) 
    { 
    	this.email = email; 
    }
    
    public Role getRole() 
    { 
    	return role; 
    }
    
    public void setRole(Role role)
    { 
    	this.role = role; 
    }
    
    public enum Role {
        CUSTOMER, ADMIN
    }
}