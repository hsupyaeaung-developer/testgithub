package com.studentregistration.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentregistration.dto.Course;
import com.studentregistration.dto.Student;
import com.studentregistration.dto.User;

@Service
public class StudentService {
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired 
	CourseRepository courseRepository;
	
	public int insertStudent(Student student,String[] courseId)
	  {
		int i = 0;
	    List<Course> courses = new ArrayList();
	    for(String id : courseId)
	    {
	    	Course course1 = courseRepository.searchCourse(id);
	      courses.add(course1);
	    }
	    student.setCourses(courses);
	    studentRepository.save(student);
	    i = 1;
	    return i;
	  }
	 public String generateStudentId() {
	        int count = studentRepository.countStudents() + 1;
	        String userId = null;
	        
	        if (count < 10) {
	            userId = "STU00" + count;
	        } else if (count >= 10 && count < 100) {
	            userId = "STU0" + count;
	        } else if (count == 100) {
	            userId = "STU" + count;
	        }
	        
	        return userId;
	    }
	 public List<Student> getAllStudentWithIsDeletedLessThanOne() {
	        return studentRepository.findAllByIsDeletedLessThan(1);
	    }
	 public Student getStudentById(String id) {
		 Student student = studentRepository.searchStudent(id);
	    	return student;
	    	}
	 
	 public int updateStudentFile(byte[] file,String fileName,long size,String id) {
		 int i = 0;
		 studentRepository.updateFile(file,fileName,size,id);
		 i = 1;
		 return i;
	 }
	 public int updateNoPhoto(Student dto,String[] courseId,String id) {
		 int i = 0;
		 List<Course> course = new ArrayList();
		 
		 Student student = studentRepository.searchStudent(id);
		    for(String cId : courseId)
		    {
		    	Course course1 = courseRepository.searchCourse(cId);
		      course.add(course1);
		    }
		    
		    dto.setFileName(student.getFileName());
		    dto.setFile(student.getFile());
		    dto.setSize(student.getSize());
		    dto.setCourses(course);
		    System.out.println(dto.getCourses().size());
		    studentRepository.save(dto);
		    i = 1;
		    return i;
	 }
	 public int updateWithPhoto(Student dto,String[] courseId,String id) {
		 int i = 0;
		    List<Course> courses = new ArrayList();
		    for(String cId : courseId)
		    {
		    	Course course1 = courseRepository.searchCourse(cId);
		      courses.add(course1);
		    }
		    dto.setCourses(courses);
		    studentRepository.save(dto);
		    i = 1;
		    return i;
	 }
	 public int deleteStudent(String id) 
	    { 
	    	int result = 0;
	    	studentRepository.markAsDeleted(1,id);
	    	result = 1;
	    	return result;
	    }
	 public List<Student> findStudent(String keyword){
		 return studentRepository.findStudent(keyword);
	 }
	 
	 public Optional<Student> findStudentById(String id){
		  return studentRepository.findById(id);
		 }
  public List<Student> studentReportPrint(){
		 return studentRepository.studentReportPrint();
 }
//	 public List<Student> getAllActiveStudentsWithCourses() throws SQLException {
//         List<Student> students = studentRepository.findAllActiveStudentsWithCourses();
//         for (Student student : students) {
//             student.setCourses(new ArrayList<>(student.getCourses()));
//         }
//         return students;
//     }
}
