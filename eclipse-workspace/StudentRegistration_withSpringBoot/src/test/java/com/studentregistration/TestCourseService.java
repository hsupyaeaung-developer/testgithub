package com.studentregistration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


import com.studentregistration.dao.CourseRepository;
import com.studentregistration.dao.CourseService;
import com.studentregistration.dto.Course;



@SpringBootTest
public class TestCourseService {

	@Mock
	CourseRepository repo;
	
	@InjectMocks
	CourseService courseService;
	
	@Test
	public void saveTest() {
		Course course=new Course();
		course.setId("COU001");
		course.setName("Java");
		courseService.save(course);
		verify(repo,times(1)).save(course);
	}
	@Test
	public void getAllBookTest() {
		List<Course> list=new ArrayList<Course>();
		Course c1=new Course();
		c1.setId("COU001");
		c1.setName("Java");
		Course c2=new Course();
		c2.setId("COU002");
		c2.setName("PHP");
		Course c3=new Course();
		c3.setId("COU002");
		c3.setName("PHP");
		list.add(c1);
		list.add(c2);
		list.add(c3);
		when(repo.findAll()).thenReturn(list);
		List<Course> courseList=courseService.getAllCourses();
		assertEquals(3,courseList.size());
		verify(repo, times(1)).findAll();
	}
	@Test
	public void getByCodeTest() {
		Course setCourse=new Course();
		setCourse.setId("COU001");
		setCourse.setName("Java");
		
		when(repo.searchCourse("101")).thenReturn(setCourse);
		Course getCourse=courseService.searchCourseById("101");
		assertEquals("COU001",getCourse.getId());
		assertEquals("Java",getCourse.getName());
		
	}
}
