package com.studentregistration;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.studentregistration.dao.CourseRepository;
import com.studentregistration.dao.StudentRepository;
import com.studentregistration.dao.StudentService;
import com.studentregistration.dao.UserRepository;
import com.studentregistration.dao.UserService;
import com.studentregistration.dto.Course;
import com.studentregistration.dto.Student;

@SpringBootTest
public class TestStudentService {

	@Mock
	StudentRepository repo;
	
	@InjectMocks
	StudentService studentService;
	
	 @Mock
	 private CourseRepository courseRepository;
	 
	 @Test
	    public void testInsertStudent() throws IOException {
	        // create a student and some course IDs to associate with them
		 	String filePath = "src/test/resources/test.txt";
		 	byte[] content = Files.readAllBytes(Paths.get(filePath));
		 	MultipartFile file = new MockMultipartFile("test.txt", "test.txt", "text/plain", content);
		 	Student student = new Student();
	        String[] courseIds = new String[] { "C001", "C002" };
	        student.setName("John Doe");
	        student.setDob("1990-01-01");
	        student.setPhone("1234567890");
	        student.setGender("Male");
	        student.setEducation("Bachelor's Degree");
	        student.setFile(file.getBytes());
	        student.setFileName(file.getOriginalFilename());
	        student.setSize(file.getSize());
	        // mock the course search results
	        Course course1 = new Course();
	        course1.setId("COU001");
	        course1.setName("Java");
	        Course course2 = new Course();
	        course2.setId("COU002");
	        course2.setName("PHP");
	        when(courseRepository.searchCourse("C001")).thenReturn(course1);
	        when(courseRepository.searchCourse("C002")).thenReturn(course2);
	        List<Course> expectedCourses = new ArrayList();
	        expectedCourses.add(course1);
	        expectedCourses.add(course2);
	        // call the method to be tested
	        int result = studentService.insertStudent(student, courseIds);
	        student.setCourses(expectedCourses);
	        // verify that the save method was called with the correct student object
	        

	        // verify that the result is 1
	        Assertions.assertEquals(1, result);

	        // verify that the student object has the correct courses set
	        
	        Assertions.assertEquals(expectedCourses, student.getCourses());
	        verify(courseRepository,times(2)).searchCourse(anyString());
	        verify(repo,times(1)).save(student);
	    }
	 @Test
	    public void testGetAllStudentWithIsDeletedLessThanOne() throws IOException {
	        // Create a list of students
	        List<Student> students = new ArrayList<>();
	        String filePath = "src/test/resources/test.txt";
		 	byte[] content = Files.readAllBytes(Paths.get(filePath));
		 	MultipartFile file = new MockMultipartFile("test.txt", "test.txt", "text/plain", content);
		 	Student student = new Student();
	        String[] courseIds = new String[] { "C001", "C002" };
	        student.setName("John Doe");
	        student.setDob("1990-01-01");
	        student.setPhone("1234567890");
	        student.setGender("Male");
	        student.setEducation("Bachelor's Degree");
	        student.setFile(file.getBytes());
	        student.setFileName(file.getOriginalFilename());
	        student.setSize(file.getSize());
	        student.setIsDeleted(0);
	        // mock the course search results
	        Course course1 = new Course();
	        course1.setId("COU001");
	        course1.setName("Java");
	        Course course2 = new Course();
	        course2.setId("COU002");
	        course2.setName("PHP");
	        when(courseRepository.searchCourse("COU001")).thenReturn(course1);
	        when(courseRepository.searchCourse("COU002")).thenReturn(course2);
	        List<Course> expectedCourses = new ArrayList();
	        expectedCourses.add(course1);
	        expectedCourses.add(course2);
	        // call the method to be tested
	        int result = studentService.insertStudent(student, courseIds);
	        student.setCourses(expectedCourses);
	        // verify that the save method was called with the correct student object
	        Student student1 = new Student();
	        
	        String[] courseId = new String[] { "C001", "C002" };
	        student1.setName("John Doe");
	        student1.setDob("1990-01-01");
	        student1.setPhone("1234567890");
	        student1.setGender("Male");
	        student1.setEducation("Bachelor's Degree");
	        student1.setFile(file.getBytes());
	        student1.setFileName(file.getOriginalFilename());
	        student1.setSize(file.getSize());
	        student1.setIsDeleted(0);
	        // mock the course search results
	        Course course3 = new Course();
	        course3.setId("COU003");
	        course3.setName("Python");
	        Course course4 = new Course();
	        course4.setId("COU004");
	        course4.setName("ASP.Net");
	        when(courseRepository.searchCourse("COU003")).thenReturn(course3);
	        when(courseRepository.searchCourse("COU004")).thenReturn(course4);
	        List<Course> expectedCourses1 = new ArrayList();
	        expectedCourses1.add(course1);
	        expectedCourses1.add(course2);
	        // verify that the result is 1
	        int result1 = studentService.insertStudent(student, courseIds);
	        student1.setCourses(expectedCourses1);
	        students.add(student1);
	        students.add(student);
	        // Mock the student repository to return the list of students
	        when(repo.findAllByIsDeletedLessThan(1)).thenReturn(students);

	        // Call the method under test
	        List<Student> list = studentService.getAllStudentWithIsDeletedLessThanOne();

	        // Verify the result
	        assertEquals(students.size(), list.size());
	        assertEquals(students.get(0).getId(), list.get(0).getId());
	        assertEquals(students.get(0).getName(), list.get(0).getName());
	        assertEquals(students.get(0).getDob(), list.get(0).getDob());
	        assertEquals(students.get(0).getEducation(), list.get(0).getEducation());
	        assertEquals(students.get(0).getFile(), list.get(0).getFile());
	        assertEquals(students.get(0).getFileName(), list.get(0).getFileName());
	        assertEquals(students.get(0).getIsDeleted(), list.get(0).getIsDeleted());
	        assertEquals(students.get(0).getPhone(), list.get(0).getPhone());
	        assertEquals(students.get(0).getCourses().size(), list.get(0).getCourses().size());
//																
	        assertEquals(students.get(1).getId(), list.get(1).getId());
	        assertEquals(students.get(1).getName(), list.get(1).getName());
	        assertEquals(students.get(1).getDob(), list.get(1).getDob());
	        assertEquals(students.get(1).getEducation(), list.get(1).getEducation());
	        assertEquals(students.get(1).getFile(), list.get(1).getFile());
	        assertEquals(students.get(1).getFileName(), list.get(1).getFileName());
	        assertEquals(students.get(1).getIsDeleted(), list.get(1).getIsDeleted());
	        assertEquals(students.get(1).getPhone(), list.get(1).getPhone());
	        assertEquals(students.get(1).getCourses().size(), list.get(1).getCourses().size());
	}
	 @Test
	    public void testGetStudentById() {
	        String id = "123";
	        Student student = new Student();
	        student.setId(id);
	        student.setName("John");

	        // Mock the behavior of the studentRepository
	        when(repo.searchStudent(id)).thenReturn(student);

	        // Call the method to be tested
	        Student actual = studentService.getStudentById(id);

	        // Verify the result
	        assertEquals(student, actual);
	    }
	 @Test
	    public void testUpdateStudentFile() {
	        String id = "123";
	        byte[] file = "test file content".getBytes();
	        String fileName = "test.txt";
	        long size = 100;
	        Student student = new Student();
	        
	        // Mock the behavior of the studentRepository
	        doNothing().when(repo).updateFile(any(byte[].class), anyString(), anyLong(), anyString());

	        // Call the method to be tested
	        int actual = studentService.updateStudentFile(file, fileName, size, id);

	        // Verify the result
	        verify(repo,times(1)).updateFile(file, fileName, size, id);
	        assertEquals(1, actual);
	    }
	 @Test
	    public void testUpdateNoPhoto() {
	        // create sample data
	        String[] courseId = new String[]{"C001", "C002"};
	        Student dto = new Student();
	        dto.setId("S001");
	        dto.setName("John Doe");
	        dto.setDob("1990-01-01");
	        dto.setPhone("1234567890");
	        dto.setGender("Male");
	        dto.setEducation("Graduate");
	        dto.setFile(new byte[]{});
	        dto.setFileName("test.txt");
	        dto.setSize(100);
	        
	        // mock repository methods
	        Student student = new Student();
	        student.setId("S001");
	        student.setFileName("test.txt");
	        student.setFile(new byte[]{});
	        student.setSize(100);
	        when(repo.searchStudent("S001")).thenReturn(student);
	        when(courseRepository.searchCourse("C001")).thenReturn(new Course());
	        when(courseRepository.searchCourse("C002")).thenReturn(new Course());
	        
	        // call service method
	        int result = studentService.updateNoPhoto(dto, courseId, "S001");
	        
	        // verify repository method calls
	        verify(repo,times(1)).searchStudent("S001");
	        verify(courseRepository,times(1)).searchCourse("C001");
	        verify(courseRepository,times(1)).searchCourse("C002");
	        verify(repo,times(1)).save(dto);
	        
	        // assert result
	        assertEquals(1, result);
	    }
	 @Test
	    public void testUpdateWithPhoto() {
	        // Arrange
	        Student dto = new Student();
	        String[] courseId = { "C001", "C002" };
	        Course course3 = new Course();
	        course3.setId("COU003");
	        course3.setName("Python");
	        Course course4 = new Course();
	        course4.setId("COU004");
	        course4.setName("ASP.Net");
	        when(courseRepository.searchCourse("COU003")).thenReturn(course3);
	        when(courseRepository.searchCourse("COU004")).thenReturn(course4);

	        // Act
	        int result = studentService.updateWithPhoto(dto, courseId, "123");

	        // Assert
	        verify(repo,times(1)).save(dto);
	        assertEquals(1, result);
	    }
	 @Test
	    public void testDeleteStudent() {
	        String studentId = "123";
	        
	        int result = studentService.deleteStudent(studentId);
	        assertEquals(1, result);
	        verify(repo,times(1)).markAsDeleted(1,studentId);
	    }
	 @Test
	 public void testFindStudent() {
	     String keyword = "John";
	     List<Student> students = Arrays.asList(new Student(), new Student());
	     when(repo.findStudent(keyword)).thenReturn(students);

	     List<Student> result = studentService.findStudent(keyword);

	     assertEquals(students.size(), result.size());
	 }
	 @Test
	    public void testStudentReportPrint() {
	        List<Student> students = Arrays.asList(new Student(), new Student());
	        when(repo.studentReportPrint()).thenReturn(students);

	        List<Student> actualStudents = studentService.studentReportPrint();

	        assertEquals(students, actualStudents);
	}
}
