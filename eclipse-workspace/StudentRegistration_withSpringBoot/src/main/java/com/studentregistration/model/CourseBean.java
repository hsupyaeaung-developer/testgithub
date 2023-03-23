package com.studentregistration.model;

import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Component;
@Component
public class CourseBean {
@NotEmpty
private String id;
@NotEmpty(message="Name must not empty!")
private String name;
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

}
