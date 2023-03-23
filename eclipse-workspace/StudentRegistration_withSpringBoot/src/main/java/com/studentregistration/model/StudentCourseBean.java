package com.studentregistration.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.web.multipart.MultipartFile;

public class StudentCourseBean {
	@NotEmpty(message="Name must not empty!")
	private String name;
	@NotEmpty(message="Id must not empty!")
	private String id;
	@NotEmpty(message="Birthday must not empty!")
	private String dob;
	@NotEmpty(message="Gender must not empty!")
	private String gender;
	@NotEmpty(message="Phone number must not empty!")
	@Pattern(regexp="^((09|\\+?959|\\+?95 9)([2-9]\\d{8}|[2-9]\\d{7}|[2-9]\\d{6}))$",message="Invalid phone number!")
	private String phone;
	@NotEmpty(message="Education must not empty!")
	private String education;
	private String fileName;
	private long size;
	private MultipartFile file;
	
	private String courses;

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

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getCourses() {
		return courses;
	}

	public void setCourses(String courses) {
		this.courses = courses;
	}

	public StudentCourseBean() {
		
	}
	
	
}
