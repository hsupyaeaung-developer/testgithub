package com.studentregistration.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.studentregistration.dao.CourseRepository;
import com.studentregistration.dao.CourseService;
import com.studentregistration.dto.Course;
import com.studentregistration.dto.Student;
import com.studentregistration.model.CourseBean;


@Controller
public class CourseController {
	@Autowired
	private CourseService courseService;
	
	@RequestMapping(value="/setupCourse",method=RequestMethod.GET)
	public ModelAndView setupaddCourse(ModelMap model,HttpSession session) {
		String courseId = courseService.generateCourseId();
		session.setAttribute("id",courseId);
		CourseBean course = new CourseBean();
		return new ModelAndView("BUD003","course",new CourseBean());
	}
	@RequestMapping(value="/menuPage",method=RequestMethod.GET)
	public String setupMenu(ModelMap model) {
		return "MNU001";
	}
	@RequestMapping(value="/courseRegister",method=RequestMethod.POST)
	public String setupAddRegister(@ModelAttribute ("course") @Validated CourseBean course,
			BindingResult bs,ModelMap model,HttpSession session) {
		session.setAttribute("msg",false);
		
		if(bs.hasErrors()) {
			session.setAttribute("courseMsg","0");
			return "BUD003";
		}
		else {
			int i = 0;
			Course dto = new Course();
			dto.setId(course.getId());
			dto.setName(course.getName());
			i = courseService.save(dto);
			if(i > 0) {
				session.setAttribute("msg",true);
				session.setAttribute("msgCourse","Registration Successful!");
				return "redirect:setupCourse";
			}
			else {
				
				model.addAttribute("bean", course);
				
				
			}
		
		}
		return "BUD003";
	}
}
