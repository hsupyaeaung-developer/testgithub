package com.studentregistration.model;

import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Component;
@Component
public class UserLoginBean {
	@NotEmpty(message="Name must not be empty!")
	private String name;
	@NotEmpty(message="Password must not be empty!")
	private String pas;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPas() {
		return pas;
	}
	public void setPas(String pas) {
		this.pas = pas;
	}

}
