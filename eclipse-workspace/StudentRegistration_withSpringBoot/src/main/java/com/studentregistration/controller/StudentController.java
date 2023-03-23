package com.studentregistration.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import com.studentregistration.dao.CourseService;
import com.studentregistration.dao.StudentService;
import com.studentregistration.dto.Course;
import com.studentregistration.dto.Student;
import com.studentregistration.model.StudentBean;
import com.studentregistration.model.StudentCourseBean;
import com.studentregistration.model.StudentSearchBean;

import model.com.message.Messaage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;




@Controller
public class StudentController {
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private CourseService courseService;
	
	@RequestMapping(value="/setUpStudentRegister" , method=RequestMethod.GET)
	public ModelAndView studentRegister(ModelMap model,HttpSession session) {
		StudentBean student= new StudentBean();
		String userId = studentService.generateStudentId();
		session.setAttribute("id",userId);
		
    	ArrayList<Course> array = (ArrayList) courseService.getAllCourses();
    	session.setAttribute("courses", array);
    	
		return new ModelAndView("STU001_01","student",new StudentBean());		
	}
	@RequestMapping(value="/studentRegister",method=RequestMethod.POST)
	public String studentRegistration(@ModelAttribute ("student") @Validated StudentBean student,
			BindingResult bs,ModelMap model,HttpSession session,HttpServletRequest request) throws IOException {
		session.setAttribute("msgReg",false);
		
		if(bs.hasErrors() || student.getFile() == null || student.getFile().isEmpty()) {
			System.out.println(bs);
			System.out.println("BindingResult!!");
			model.addAttribute("fileError", "Upload file is empty!");
			String[] stuCourse = student.getCourses();
			ArrayList list = new ArrayList();
			if(stuCourse != null) {
				for(int i = 0;i < stuCourse.length;i++) {
					list.add(stuCourse[i]);
				}
			}
			model.addAttribute("list", list);
			return "STU001_01";
		}
		else {
				String name = student.getFile().getOriginalFilename();
				student.setFileName(name);
				
				
	    		Student dto = new Student();
	    		dto.setId(student.getId());
	    		dto.setName(student.getName());
	    		dto.setDob(student.getDob());
	    		dto.setPhone(student.getPhone());
	    		dto.setGender(student.getGender());
	    		dto.setEducation(student.getEducation());
	    		dto.setFile(student.getFile().getBytes());
	    		dto.setFileName(student.getFileName());
	    		String[] courseId = student.getCourses();
	    		dto.setSize(student.getFile().getSize());
	    		int i = studentService.insertStudent(dto,courseId);
	    		System.out.println(i);
	    		if(i > 0) {
	    			session.setAttribute("msgReg",true);
	    			session.setAttribute("stuReg", "Registration Successful!");
	    			
	    			
	    		}
	    		else {
	    			System.out.println("Failed!!");
	    			
//	    			request.setAttribute("regCourse", student.getStuAttend());
	    			model.addAttribute("bean", student);
	    			return "STU001_01";
	    		}
	    		
	    		return "redirect:setUpStudentRegister";
		}		
		
	}
	@RequestMapping(value="/showStudent",method=RequestMethod.GET)
	public ModelAndView showStudent(ModelMap model,HttpSession session) {
		StudentSearchBean search = new StudentSearchBean();
		ArrayList<Student> list = (ArrayList) studentService.getAllStudentWithIsDeletedLessThanOne();
		
		session.setAttribute("list", list);
		return new ModelAndView("STU003","search",new StudentSearchBean());
	}
	@RequestMapping(value="/studentSearch",method=RequestMethod.POST)
	public String searchEmployee(@ModelAttribute ("search") StudentSearchBean search,ModelMap model,HttpSession session) {
		session.setAttribute("stuError", "0");	
		session.setAttribute("stuSearchError", "0");	
		
			String keyword = search.getId();
			session.setAttribute("keyword", keyword);
			
			System.out.println(keyword);
			ArrayList<Student> list = (ArrayList)studentService.findStudent(keyword);
			System.out.println(list.size());
			
//			if(!(course.isEmpty() || course.isBlank())) {
//				if(list.size() == 0) {
//					list =  dao.SearchwithCourse(dto.getAttend());
//				}
//			}
//			System.out.println(list.size());
//			if(list.size() == 0) {
//				
//				model.addAttribute("list", list);
//				model.addAttribute("stuSearchError", "1");
//				
//			}
			if(list.size() == 0) {
				System.out.println("Failed!!");
			}
			session.setAttribute("list", list);
		return"STU003";
	}
	@RequestMapping(value="/setupUpdateStudent/{id}",method=RequestMethod.GET)
	public ModelAndView setupUpdateUser(@PathVariable String id,ModelMap model,HttpSession session) {
		System.out.println(id);
		ArrayList<Course> array = (ArrayList) courseService.getAllCourses();
    	session.setAttribute("courses", array);
    	Student student = studentService.getStudentById(id);
        
    	String fileName = student.getFileName();
		List<Course> list = student.getCourses();
		ArrayList list1 = new ArrayList();
		for(int i = 0;i < list.size();i++) {
			System.out.println(list.get(i).getId());
			list1.add(list.get(i).getId());
		}
		model.addAttribute("list", list1);
		session.setAttribute("fileName",fileName);
		return new ModelAndView("STU002_01","student",studentService.getStudentById(id));
	}
	
	@RequestMapping(value="/updateStudent",method=RequestMethod.POST)
	public String updateUser(@ModelAttribute ("student") @Validated StudentBean student,
			BindingResult bs,ModelMap model,HttpServletRequest request,HttpSession session) throws IOException {
		
		
		if(bs.hasErrors()) {
			
			if(student.getFile().isEmpty()) {
				String fileName = (String)session.getAttribute("fileName");
				session.setAttribute("fileName",fileName);
				System.out.println(fileName);
				String[] c = student.getCourses();
	    		ArrayList list1 = new ArrayList();
	    		for(int i = 0;i < c.length;i++) {
	    			
	    			list1.add(c[i]);
	    		}
	    		model.addAttribute("list", list1);
				System.out.println("Null!");
				
			}
			if(!student.getFile().isEmpty()) {
				System.out.println("Not Null!");
				MultipartFile multipartFile = student.getFile();
				String name = multipartFile.getOriginalFilename();				
				session.setAttribute("fileName", name);
				Student dto = new Student();
				dto.setId(student.getId());
	     		dto.setFile(multipartFile.getBytes());
				dto.setFileName(name);
				dto.setSize(multipartFile.getSize());
				String id = dto.getId();
				byte[] file = dto.getFile();
				long size = dto.getSize();
				String fileName = dto.getFileName();
				System.out.println(dto.getFileName());
				
				String[] c = student.getCourses();
	    		ArrayList list1 = new ArrayList();
	    		for(int i = 0;i < c.length;i++) {
	    			
	    			list1.add(c[i]);
	    		}
	    		model.addAttribute("list", list1);
				int i = studentService.updateStudentFile(file,fileName,size,id);
				if(i > 0) {
					System.out.println("File Upload successful!");				
				}
			}
	
			
			return "STU002_01";
		}
		else {
			Student dto = new Student();
				MultipartFile file = student.getFile();
				String name = file.getOriginalFilename();
				
	    		int j = 0,i = 0;
	    		
	    		
	    		dto.setId(student.getId());
	    		dto.setName(student.getName());
	    		dto.setDob(student.getDob());
	    		dto.setPhone(student.getPhone());
	    		dto.setGender(student.getGender());
	    		dto.setEducation(student.getEducation());
	    		dto.setFile(student.getFile().getBytes());
	    		dto.setFileName(name);
	    		String[] courseId = student.getCourses();
	    		
	    		dto.setSize(student.getFile().getSize());
	    		
	    	
	    		if(dto.getFileName() == null || dto.getFileName() == "" || dto.getFileName().isEmpty()) {
	    			
	    			String name1 = (String)session.getAttribute("fileName");
	    			System.out.println(name1);
	    			dto.setFileName(name1);
	    			
	    			j = studentService.updateNoPhoto(dto,courseId,dto.getId());
	    			model.addAttribute("msg", true);
	    			model.addAttribute("msgUpd", "Update Successful!!");

	    		}
	    		else {
	    			i = studentService.updateWithPhoto(dto,courseId,dto.getId());
	    			model.addAttribute("msg", true);
	    			model.addAttribute("msgUpd", "Update Successful!!");
	    		}
	    		
	    		if(i > 0 || j > 0) {
	    			if(i > 0) {
	    				session.setAttribute("fileName", name);
	    				
	    			}
	    			if(j > 0) {
	    				session.setAttribute("fileName", dto.getFileName());
	    			}
	   
	    		}
	    		
	    		else {
	    			
	    			request.setAttribute("errorUpdate", "Update Failed!!");
	    			
	    		}
	    		String[] c = student.getCourses();
	    		ArrayList list1 = new ArrayList();
	    		for(i = 0;i < c.length;i++) {
	    			
	    			list1.add(c[i]);
	    		}
	    		model.addAttribute("list", list1);
			return "STU002_01";
		}		
}
	@RequestMapping(value="deleteStudent/{id}",method=RequestMethod.GET)
	public String deleteStudent(@PathVariable String id,ModelMap model,HttpSession session) {

		int i = studentService.deleteStudent(id);
		if(i > 0) {
			session.setAttribute("msg", true);
			session.setAttribute("msgDel", "Delete Successful!!");
		}
		else {
			session.setAttribute("error", true);
			session.setAttribute("errorDel", "Delete Failed");
		}
		return "redirect:/showStudent";
	}
	 @GetMapping("/downloadfile")
	 public void downloadFile(@Param("id") String id , Model model, HttpServletResponse response) throws IOException {
	  Optional<Student> temp = studentService.findStudentById(id);
	  if(temp!=null) {
	   Student student = temp.get();
	   response.setContentType("application/octet-stream");
	   String headerKey = "Content-Disposition";
	   String headerValue = "attachment; filename = "+student.getFileName();
	   response.setHeader(headerKey, headerValue);
	   ServletOutputStream outputStream = response.getOutputStream();
	   outputStream.write(student.getFile());
	   outputStream.close();
	  }
	 }
	 
	 @GetMapping("/image")
	 public void showImage(@Param("id") String id, HttpServletResponse response, Optional<Student> student, HttpSession sesssion)
	   throws ServletException, IOException {
	  
	  student = studentService.findStudentById(id);
	  response.setContentType("image/jpeg, image/jpg, image/png, image/gif, image/pdf");
	  response.getOutputStream().write(student.get().getFile());
	  response.getOutputStream().close();
	 }
	 @RequestMapping(value="/printFile", 
			 method=RequestMethod.GET)
			 public String Book(ModelMap model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		 List<Student> list=studentService.studentReportPrint();
			System.out.println(list.size());
			session.setAttribute("studentList", list);
			String keyword = (String)session.getAttribute("keyword");
			if(keyword != null ) {
				List<Student> searchList = (ArrayList)studentService.findStudent(keyword);
				session.setAttribute("studentList", searchList);
			}
		
			 	String filePath = "C:\\Users\\YOGA\\eclipse-workspace\\StudentRegistration_withSpringBoot\\src\\main\\resources\\StudentList.jrxml";
			 	Map parameters = new HashMap();
			 	parameters.put("ReportTitle", "Student List");
		
			 	if (session.getAttribute("studentList") != null) {
			 	list = (List<Student>) session.getAttribute("studentList");
			 	}
			 	JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(list);
			 	JasperReport report;
			 	try {
			 		report = JasperCompileManager.compileReport(filePath);
			 		JasperPrint print = JasperFillManager.fillReport(report, parameters, source);
			 		JasperExportManager.exportReportToPdfFile(print,"C:\\Users\\YOGA\\Downloads\\StudentList.pdf");
			 		System.out.println("Report Created!");
			 		session.setAttribute("message", new Messaage("Successfully printed.","success"));
			 		
			 	} catch (JRException e) {
			 		// TODO Auto-generated catch block
			 		e.printStackTrace();
			 		session.setAttribute("error", new Messaage("Failed!!","failed"));
			 	}
			 	session.setAttribute("keyword", null);
			 return "redirect:showStudent";

			 }
}
