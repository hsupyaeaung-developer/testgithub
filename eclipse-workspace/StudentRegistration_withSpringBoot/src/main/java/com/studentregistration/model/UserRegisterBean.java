package com.studentregistration.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.stereotype.Component;
@Component
public class UserRegisterBean {
@NotEmpty(message="Email must not be empty!")
@Pattern(regexp="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])" , message="Invalid email address")
private String email;
@NotEmpty(message="Password not be empty!")
private String pas;
@NotEmpty(message="Confirm Password not be empty!")
private String c_pas;
@NotEmpty(message="Role not be empty!")
private String role;
@NotEmpty(message="Name not be empty!")
private String name;
@NotEmpty(message="Id not be empty!")
private String id;
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPas() {
	return pas;
}
public void setPas(String pas) {
	this.pas = pas;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getC_pas() {
	return c_pas;
}
public void setC_pas(String c_pas) {
	this.c_pas = c_pas;
}

}
