package com.studentregistration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.studentregistration.dto.Course;
import com.studentregistration.dto.User;

public interface CourseRepository extends JpaRepository<Course, String> 
{
	@Query(value = "SELECT COUNT(*) FROM Course")
	 int countCourses();
	
	@Query("SELECT c FROM Course c WHERE c.id=?1")
	 public Course searchCourse(String id);
}