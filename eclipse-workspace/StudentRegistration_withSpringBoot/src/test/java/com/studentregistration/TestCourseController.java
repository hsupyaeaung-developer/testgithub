package com.studentregistration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.studentregistration.dao.CourseRepository;
import com.studentregistration.dao.CourseService;
import com.studentregistration.model.CourseBean;



@SpringBootTest
@AutoConfigureMockMvc
public class TestCourseController {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	CourseService bookService;
	
	@MockBean
	CourseRepository repo;
	
	@Test
	public void testsetupaddCourse() throws Exception {
		this.mockMvc.perform(get("/setupCourse"))
		.andExpect(status().isOk())
		.andExpect(view().name("BUD003"))
		.andExpect(model().attributeExists("course"));
	}
	@Test
	public void testsetupMenu() throws Exception {
		this.mockMvc.perform(get("/menuPage"))
		.andExpect(status().isOk())
		.andExpect(view().name("MNU001"));
		
	}
	@Test
	public void testaddCourse() throws Exception {
		CourseBean course=new CourseBean();
		course.setId("COU001");
		course.setName("Java");
		this.mockMvc.perform(post("/courseRegister").flashAttr("course", course))
		.andExpect(status().isOk())
		.andExpect(view().name("BUD003"));	
	}
}
