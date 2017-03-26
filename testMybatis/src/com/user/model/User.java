package com.user.model;

public class User {
     private  int  id;
     private String  username;
     private String password;
     private String  email;
     private String  address;
     private String  phone;
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password + ", email=" + email + ", address=" + address
				+ ", phone=" + phone + "]";
	}
	public User(int id, String username, String password, String email,
			String address, String phone) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.address = address;
		this.phone = phone;
	}
	public User(String username, String password, String email, String address,
			String phone) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.address = address;
		this.phone = phone;
	}
	public User() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
