package com.bezkoder.springjwt.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Urls {
	
	 @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	 
	  @NotBlank
	  @Size(max = 20)
	  @Column(name = "short_code")
	  private String shortCode;
	  
	  
	  @NotBlank
	  private String long_url;
	  
	  @Column(name = "created_at")
	  private LocalDateTime createdAt = LocalDateTime.now();
	  
	  
	    @Column(name = "clicks", nullable = false)
	    private int clicks = 0;
	  
	  @ManyToOne
	  private User user_id;

	  
	  
	public Urls() {
		
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getShortCode() {
		return shortCode;
	}


	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}


	public String getLong_url() {
		return long_url;
	}


	public void setLong_url(String long_url) {
		this.long_url = long_url;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}


	public User getUser_id() {
		return user_id;
	}


	public void setUser_id(User user_id) {
		this.user_id = user_id;
	}


	public int getClicks() {
		return clicks;
	}


	public void setClicks(int clicks) {
		this.clicks = clicks;
	}
	  
	  
	  
	  
	  
	 
	
	

}
