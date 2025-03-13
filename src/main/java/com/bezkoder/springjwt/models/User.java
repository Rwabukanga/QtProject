package com.bezkoder.springjwt.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;



@Entity
@Table(name = "users", 
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email") 
    })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 20)
  private String username;

  //@NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(  name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();
  
  

  @Column(name = "created_at")
  private LocalDateTime createdAt = LocalDateTime.now();
  
  @ManyToOne
  private Registrar reg;

  public User() {
  }

  



public User(Long id, @NotBlank @Size(max = 20) String username, @Size(max = 50) @Email String email,
		@NotBlank @Size(max = 120) String password, Set<Role> roles, LocalDateTime createdAt, Registrar reg) {
	super();
	this.id = id;
	this.username = username;
	this.email = email;
	this.password = password;
	this.roles = roles;
	this.createdAt = createdAt;
	this.reg = reg;
}





public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
 
public LocalDateTime getCreatedAt() {
	return createdAt;
}



public void setCreatedAt(LocalDateTime createdAt) {
	this.createdAt = createdAt;
}



public Registrar getReg() {
	return reg;
}

public void setReg(Registrar reg) {
	this.reg = reg;
}
  
  
}
