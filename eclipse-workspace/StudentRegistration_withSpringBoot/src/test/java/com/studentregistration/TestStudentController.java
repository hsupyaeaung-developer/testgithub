package com.studentregistration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.studentregistration.controller.StudentController;
import com.studentregistration.controller.UserController;
import com.studentregistration.dao.CourseService;
import com.studentregistration.dao.StudentRepository;
import com.studentregistration.dao.StudentService;
import com.studentregistration.dao.UserRepository;
import com.studentregistration.dao.UserService;
import com.studentregistration.dto.Course;
import com.studentregistration.dto.Student;
import com.studentregistration.dto.User;
import com.studentregistration.model.StudentBean;
import com.studentregistration.model.StudentSearchBean;
import com.studentregistration.model.UserLoginBean;
import com.studentregistration.model.UserRegisterBean;

import junit.framework.Assert;

@SpringBootTest
@AutoConfigureMockMvc
public class TestStudentController {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	StudentService studentService;
	
	
	
	@MockBean
	StudentRepository repo;
	
	 @InjectMocks
	    private StudentController studentController;
	    
	    @Mock
	    private StudentSearchBean searchbean;
	    
	    @Mock
	    private BindingResult bs;
	    
	   
	    
	    @Mock
	    private HttpSession session;
	    
	    @Mock
	    private ModelMap model;
	    
	    @Mock
	    private StudentBean student;
	    
	    @Mock
	    private BindingResult bindingResult;
	    
	    
		private HttpServletRequest request;
		
	    @Test
		public void testsetupLogin() throws Exception {
			this.mockMvc.perform(get("/setUpStudentRegister"))
			.andExpect(status().isOk())
			.andExpect(view().name("STU001_01"))
			.andExpect(model().attributeExists("student"));
		}
	    @Before
		public void setup() {
			session = mock(HttpSession.class);
			request = mock(HttpServletRequest.class);
			bindingResult = mock(BindingResult.class);
			model = mock(ModelMap.class);
		}
		
	    @Test
	    public void testsetStudentvalidate() throws Exception {
	        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes());
	        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/studentRegister")
	                .file(file)
	                .param("id", "1")
	                .param("name", "John")
	                .param("dob", "01/01/2000")
	                .param("phone", "09764510308")
	                .param("gender", "M")
	                .param("education", "Bachelor's Degree")
	                .param("courses", "C001")
	                .param("courses", "C002");

	        mockMvc.perform(builder)
	                .andExpect(status().isOk())
	                .andExpect(view().name("STU001_01"));
	    }
	    @Test
	    public void testStudentRegistration() throws Exception {
	        // Create a mock session
	        MockHttpSession session = new MockHttpSession();
	        
	        // Create a mock StudentBean object
	        StudentBean student = new StudentBean();
	        student.setId("123");
	        student.setName("John Doe");
	        student.setDob("1990-01-01");
	        student.setGender("M");
	        student.setPhone("09797500387");
	        student.setEducation("Bachelor's degree");
	        student.setCourses(new String[] {"1", "2"});
	        
	        // Mock the MultipartFile object
	        
	        
	        
	        // Perform the POST request
	        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
	        student.setFile(file);
	        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/studentRegister")
	                .file(file)
	                .param("id", "1234")
	                .param("name", "John Doe")
	                .param("dob", "01/01/2000")
	                .param("gender", "Male")
	                .param("phone", "09123456789")
	                .param("education", "Bachelor")
	                .param("courses", "Maths", "Science"))
	                .andExpect(status().isOk())
	                .andReturn();
	        
	        
	    }


	    @Test
		public void testShowStudent() throws Exception {
			StudentSearchBean search = new StudentSearchBean();
			ArrayList<Student> list = new ArrayList<Student>();
			student = new StudentBean();
			student.setId("1");
			student.setName("John");
			student.setDob("01/01/1990");
			student.setPhone("09764510308");
			student.setGender("Male");
			student.setEducation("Graduate");
			String filePath = "src/test/resources/test.txt";
		 	byte[] content = Files.readAllBytes(Paths.get(filePath));
		 	MultipartFile file = new MockMultipartFile("test.txt", "test.txt", "text/plain", content);
			student.setFile(file);
		    
		   
			String[] courses = {"Math", "Science"};
			student.setCourses(courses);
			Student student1 = new Student();
			student1.setId(student.getId());
			student1.setName(student.getName());
			student1.setDob(student.getDob());
			student1.setPhone(student.getPhone());
			student1.setGender(student.getGender());
			student1.setEducation(student.getEducation());
			student1.setFile(student.getFile().getBytes());
			student1.setFileName(student.getFile().getOriginalFilename());
			student1.setSize(student.getFile().getSize());
			
			list.add(student1);
			when(studentService.getAllStudentWithIsDeletedLessThanOne()).thenReturn(list);
			
			  this.mockMvc.perform(get("/showStudent"))
				.andExpect(status().isOk())
				.andExpect(view().name("STU003"))
				.andExpect(model().attributeExists("search"));
			  verify(studentService, times(1)).getAllStudentWithIsDeletedLessThanOne();
		}
	    @Test
		public void testSearchEmployee() throws Exception {
			String keyword = "123";
			
			ArrayList<Student> list = new ArrayList<Student>();
			student = new StudentBean();
			student.setId("123");
			student.setName("John");
			student.setDob("01/01/1990");
			student.setPhone("09764510308");
			student.setGender("Male");
			student.setEducation("Graduate");
			String filePath = "src/test/resources/test.txt";
		 	byte[] content = Files.readAllBytes(Paths.get(filePath));
		 	MultipartFile file = new MockMultipartFile("test.txt", "test.txt", "text/plain", content);
			student.setFile(file);
		    
		   
			String[] courses = {"Math", "Science"};
			student.setCourses(courses);
			Student student1 = new Student();
			student1.setId(student.getId());
			student1.setName(student.getName());
			student1.setDob(student.getDob());
			student1.setPhone(student.getPhone());
			student1.setGender(student.getGender());
			student1.setEducation(student.getEducation());
			student1.setFile(student.getFile().getBytes());
			student1.setFileName(student.getFile().getOriginalFilename());
			student1.setSize(student.getFile().getSize());
			
			list.add(student1);
			when(studentService.findStudent(keyword)).thenReturn(list);
			
			StudentSearchBean search = new StudentSearchBean();
			search.setId(keyword);
			
			mockMvc.perform(post("/studentSearch")
	                .param("id", keyword))
	                .andExpect(status().isOk())
	                .andExpect(view().name("STU003"));
	               
	 
	        verify(studentService, times(1)).findStudent(keyword);
		}
	    @Test
		public void testSetUpdateValidate() throws Exception {
			MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes());
	        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/updateStudent")
	                .file(file)
	                .param("id", "1")
	                .param("name", "John")
	                .param("dob", "01/01/2000")
	                .param("phone", "09764510308")
	                .param("gender", "M")
	                .param("education", "Bachelor's Degree")
	                .param("courses", "C001")
	                .param("courses", "C002");

	        mockMvc.perform(builder)
	                .andExpect(status().isOk())
	                .andExpect(view().name("STU002_01"));
		}
		
		@Test
		public void testupdateStudentok() throws Exception {
			StudentBean stu = new StudentBean();
	        stu.setId("123");
	        stu.setName("John Doe");
	        stu.setDob("1990-01-01");
	        stu.setGender("M");
	        stu.setPhone("1234567890");
	        stu.setEducation("Bachelor's degree");
	        stu.setCourses(new String[] {"1", "2"});
	        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());
	        stu.setFile(file);
			this.mockMvc.perform(post("/updateStudent").flashAttr("student", stu))
			.andExpect(status().isOk())
			.andExpect(view().name("STU002_01"))
			 .andReturn();
		}
		@Test
		public void testDelete() throws Exception {
		    // create a mock HttpSession object
		    MockHttpSession session = new MockHttpSession();
		    session.setAttribute("id", "123");

		    // perform the delete request
		    mockMvc.perform(get("/deleteStudent/{id}", "123")
		            .session(session))
		        .andExpect(status().is(302))
		        .andExpect(redirectedUrl("/showStudent"));

		    // verify that the softDelete method was called once in UserService with the correct argument
		    verify(studentService, times(1)).deleteStudent("123");
		}
		
		
}
