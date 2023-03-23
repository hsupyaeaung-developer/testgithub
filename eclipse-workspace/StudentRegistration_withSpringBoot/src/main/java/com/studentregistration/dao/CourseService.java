package com.studentregistration.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentregistration.dto.Course;
import com.studentregistration.dto.User;

@Service
public class CourseService {
	@Autowired
	CourseRepository courseRepository;
	public int save(Course dto) 
	{ 
		int i = 0;
		courseRepository.save(dto); 
		i = 1;
		return i;
	} 
	 public String generateCourseId() {
	        int count = courseRepository.countCourses() + 1;
	        String courseId = null;
	        
	        if (count < 10) {
	        	courseId = "COU00" + count;
	        } else if (count >= 10 && count < 100) {
	        	courseId = "COU0" + count;
	        } else if (count == 100) {
	        	courseId = "COU" + count;
	        }
	        
	        return courseId;
	    }
	 public Course searchCourseById(String id) {
		 Course course = courseRepository.searchCourse(id);
		 
		 return course;
	 }
	 public List<Course> getAllCourses() 
	 { 
	 List<Course> list = (List<Course>) courseRepository.findAll();
	 return list;
	 } 
}
