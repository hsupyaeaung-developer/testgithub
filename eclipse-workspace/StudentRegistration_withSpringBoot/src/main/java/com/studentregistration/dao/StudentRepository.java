package com.studentregistration.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.studentregistration.dto.Course;
import com.studentregistration.dto.Student;
import com.studentregistration.dto.User;
import com.studentregistration.model.StudentCourseBean;

@Repository
public interface StudentRepository extends JpaRepository<Student, String>{

	 @Query(value = "SELECT COUNT(*) FROM Student")
	 int countStudents();
	 
	 List<Student> findAllByIsDeletedLessThan(int isDeleted);
	 
	 @Query("SELECT s from Student s where s.id=?1")
	  Student searchStudent(String id);
	 
	 @Modifying
	 @Transactional
	 @Query("UPDATE Student s SET s.file=?1,s.fileName=?2,s.size=?3 WHERE s.id=?4")
	 void updateFile(byte[] file,String fileName,long size,String id);
	 
	  @Modifying
	  @Transactional
	  @Query("UPDATE Student s SET s.isDeleted = ?1 WHERE s.id = ?2")
	  void markAsDeleted(int isDeleted,String id);
	  
	 @Query("SELECT DISTINCT s FROM Student s JOIN s.courses c WHERE (s.name LIKE %?1% OR c.name LIKE %?1% OR s.id LIKE %?1%)AND s.isDeleted = 0")
	 List<Student> findStudent(String keyword);
	  
     @Modifying
	 @Transactional
     //@Query("SELECT s.name, s.phone, s.education, s.fileName, s.gender, s.dob, GROUP_CONCAT(c.name SEPARATOR ',')  as courses FROM Student s JOIN Student_course sc ON s.id = sc.student_id JOIN Course c ON sc.course_id = c.id WHERE s.isDeleted = 0 GROUP BY s.id")
	 @Query("SELECT DISTINCT s FROM Student s JOIN FETCH s.courses WHERE s.isDeleted = 0")
	 List<Student> studentReportPrint();
     
//	 @Modifying
//	 @Transactional
//	 @Query("SELECT NEW com.studentregistration.dto.Student(s.name, s.phone, s.education, s.fileName, s.gender, s.dob, GROUP_CONCAT(c.name SEPARATOR ',') as courses) FROM student s JOIN student_course sc ON s.id = sc.student_id JOIN course c ON sc.course_id = c.id WHERE s.isDeleted = 0 GROUP BY s.id")
//	 public List<Student> getStudentReportDTO();

//	 @Modifying
//	 @Transactional
//	 @Query("SELECT s FROM Student s JOIN FETCH s.courses WHERE s.isDeleted = 0")
//     List<Student> findAllActiveStudentsWithCourses();
}
