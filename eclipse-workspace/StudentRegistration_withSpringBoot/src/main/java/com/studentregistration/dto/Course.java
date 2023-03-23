package com.studentregistration.dto;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="course")
public class Course {
@Id
private String id;
private String name;
@ManyToMany(mappedBy = "courses")
private List<Student> students = new ArrayList<>();
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

public List<Student> getStudents() {
	return students;
}
public void setStudents(List<Student> students) {
	this.students = students;
}
public Course() {
	
}
@Override
public String toString() {
	return name;
}

}
