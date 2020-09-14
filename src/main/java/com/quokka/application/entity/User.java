package com.quokka.application.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.sym.Name;

@Entity
@Table(name = "users")
public class User {
	
	public User() {
		
	}
	
	public User(User user) {
		
		this.userId = user.getUserId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.phoneNumber = user.getPhoneNumber();
		this.password = user.getPassword();
		this.otp = user.getOtp();
		this.createdBy = user.getCreatedBy();
		this.updatedBy = user.getUpdatedBy();
		this.createdOn = user.getCreatedOn();
		this.updatedOn = user.getUpdatedOn();
		this.isDeleted = user.getIsDeleted();
		this.roles = user.getRoles();
		
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userid")
	private int userId;
	
	@Column(name = "username")
	private String username;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "otp")
	private Integer otp;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdon")
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatedon")
	private Date updatedOn;

	@Column(name = "createdby")
	private int createdBy;
	
	@Column(name = "updatedby")
	private int updatedBy;

	@Column(name = "isdeleted", columnDefinition = "BIT")
	private Boolean isDeleted;
	
	@JsonManagedReference
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns =@JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid")
            )
	private List<Role> roles = new ArrayList<Role>();

	public int getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return this.email;
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

	
	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", phoneNumber=" + phoneNumber + ", otp=" + otp + ", createdBy=" + createdBy + ", updatedBy="
				+ updatedBy + ", isDeleted=" + isDeleted + ", roles=" + roles + "]";
	}

	
	
	
}
