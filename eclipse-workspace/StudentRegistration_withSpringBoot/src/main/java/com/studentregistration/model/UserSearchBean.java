package com.studentregistration.model;

import org.springframework.stereotype.Component;

@Component
public class UserSearchBean {
private String id;
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
