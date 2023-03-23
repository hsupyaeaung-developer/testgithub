package com.studentregistration.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import com.studentregistration.dao.StudentService;
import com.studentregistration.dao.UserService;
import com.studentregistration.dto.Student;
import com.studentregistration.dto.User;
import com.studentregistration.model.StudentCourseBean;
import com.studentregistration.model.UserLoginBean;
import com.studentregistration.model.UserRegisterBean;
import com.studentregistration.model.UserSearchBean;


@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private StudentService studentService;
	
	
	@RequestMapping(value="/" , method=RequestMethod.GET)
	public ModelAndView Login(ModelMap model,HttpServletRequest request,HttpSession session) throws SQLException {
		
		UserLoginBean userLogin= new UserLoginBean();
		return new ModelAndView("LGN001","userLogin",new UserLoginBean());		
	}
	@RequestMapping(value="/logOut" , method=RequestMethod.GET)
	public ModelAndView logOut(ModelMap model,HttpSession session) {
		session.invalidate();
		UserLoginBean userLogin= new UserLoginBean();
		return new ModelAndView("LGN001","userLogin",new UserLoginBean());		
	}
	@RequestMapping(value="/userLogin",method=RequestMethod.POST)
	public String UserLogin(@ModelAttribute ("userLogin") @Validated UserLoginBean bean,
			BindingResult bs,ModelMap model,HttpSession session) {
		if(bs.hasErrors()) {
			System.out.println("Here!!");
			return "LGN001";
		}
		else {
			
			User user = new User();
			user.setName(bean.getName());
			user.setPas(bean.getPas());
			User user1 = new User();
			user1 = userService.CheckUser(user.getName(),user.getPas());
			if(user1 == null)
			{
				model.addAttribute("loginError", "Login Failed!");
				return "LGN001";
			}
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");  
		    Date date = new Date();
		    String today = formatter.format(date);
			//String today = date.toString();
		    session.setAttribute("date", today);
			if(!user1.equals(null)) {
				System.out.println(user.getRole());
				session.setAttribute("id", user1.getId());
				session.setAttribute("name", user1.getName());
				session.setAttribute("role", user1.getRole());
				
				session.setAttribute("value", true);
			}
			return "MNU001";
		}		
	}
	@RequestMapping(value="/userEmail" , method=RequestMethod.GET)
	public ModelAndView emailLogin(ModelMap model,HttpServletRequest request,HttpSession session) throws SQLException {
		
		UserRegisterBean userLogin= new UserRegisterBean();
		return new ModelAndView("LGN002","userEmail",new UserRegisterBean());		
	}
	@RequestMapping(value="/emailCheck",method=RequestMethod.POST)
	public String UserEmail(@ModelAttribute ("userEmail") UserRegisterBean bean,ModelMap model,HttpSession session) {
		List<User> list = userService.getAllUsersWithIsDeletedLessThanOne();
		int result = 0;
		if(bean.getEmail() != null) {
			for(int i = 0;i < list.size();i++) {
				if(list.get(i).getEmail().equals(bean.getEmail())) {
					 result = 1;
				String userId = list.get(i).getId();
				session.setAttribute("userId", userId);
				}
			}
		}
		if(result == 0) {
			model.addAttribute("msg", true);
			model.addAttribute("msg", "Invalid Email!!");
			return "LGN002";
		}
		return "redirect:/pasCreate";
	}
	@RequestMapping(value="/pasCreate" , method=RequestMethod.GET)
	public ModelAndView pasCreate(ModelMap model,HttpServletRequest request,HttpSession session) throws SQLException {
		
		UserRegisterBean userLogin= new UserRegisterBean();
		return new ModelAndView("LGN003","userPas",new UserRegisterBean());		
	}
	@RequestMapping(value="/pasCheck",method=RequestMethod.POST)
	public String UserPas(@ModelAttribute ("userPas") UserRegisterBean bean,ModelMap model,HttpSession session) {
		if(!bean.getC_pas().equals(bean.getPas())) {
			model.addAttribute("pasError", "Password is not match!");
			return "LGN003";
		}
		String id = (String)session.getAttribute("userId");
		System.out.println(id);
		String pas = bean.getPas();
		System.out.println(pas);
		int i = 0;
		i  = userService.insertUserWithPas(pas,id);
		if(i == 0) {
			return "LGN003";
		}
		return "redirect:/";
	}
	@RequestMapping(value="/setUpUserRegister" , method=RequestMethod.GET)
	public ModelAndView USR001(ModelMap model,HttpSession session) {
		String userId = userService.generateUserId();
		session.setAttribute("id",userId);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");  
	    Date date = new Date();
	    String today = formatter.format(date);
		//String today = date.toString();
	    session.setAttribute("date", today);
		UserRegisterBean userRegister= new UserRegisterBean();
		
		return new ModelAndView("USR001","userRegister",new UserRegisterBean());		
	}
	@RequestMapping(value="/userRegister",method=RequestMethod.POST)
	public String UserRegister(@Param("id") String id,@ModelAttribute ("userRegister") @Validated UserRegisterBean bean,
			BindingResult bs,ModelMap model,HttpSession session) {
		
		session.setAttribute("msgReg",false);
		
		List<User> list = userService.getAllUsersWithIsDeletedLessThanOne();
		if(bean.getEmail() != null) {
			for(int i = 0;i < list.size();i++) {
				if(list.get(i).getEmail().equals(bean.getEmail())) {
					model.addAttribute("msgEmail",true);
					model.addAttribute("msgEmail", "Email already have exit!!");
					return "USR001";
				}
			}
		}
		if(bs.hasErrors()) {	
			return "USR001";
		}
		
		else {
			if(!bean.getC_pas().equals(bean.getPas())) {
				model.addAttribute("pasError", "Password is not match!");
				return "USR001";
			}
			
			
			else {
				User dto = new User();
				dto.setId(id);
				dto.setName(bean.getName());
				dto.setEmail(bean.getEmail());
				dto.setPas(bean.getPas());
				dto.setRole(bean.getRole());
				User user = userService.insertUser(dto);
				//User = service.save(dto);
				//if(User != null)
				if(user != null) {
					session.setAttribute("msgReg",true);
					session.setAttribute("msgReg", "Registration Successful.");
					return "redirect:setUpUserRegister";
				}
				else {
					session.setAttribute("bean", bean);
					model.addAttribute("error", "Registration Failed!");
				}
				
			}
			return "USR001";
		}		
	}
	@RequestMapping(value="/showUser",method=RequestMethod.GET)
	public ModelAndView setupUserSearch(ModelMap model,HttpSession session) {
		UserSearchBean search = new UserSearchBean();
		ArrayList<User> list = (ArrayList<User>)userService.getAllUsersWithRoleAndIsDeletedLessThanOne();	
		model.addAttribute("list", list);		
		return new ModelAndView("USR003","search",new UserSearchBean());
	}
	@RequestMapping(value="/setupUserSearch",method=RequestMethod.POST)
	public String searchUser(@ModelAttribute ("search") @Validated UserSearchBean search,ModelMap model,HttpSession session) {
	session.setAttribute("msgBlank", false);
			System.out.println(search.getId());
			System.out.println(search.getName());
				User dto = new User();
				dto.setName(search.getName());
				dto.setId(search.getId());
				int isDeleted = 1;
				ArrayList<User> list = (ArrayList<User>)userService.searchData(dto.getId(),dto.getName());
				if(list.size() == 0) {
					System.out.println("Searching Failed!");
					model.addAttribute("msgNotFound", true);
					model.addAttribute("searchError", "User Data not found!!");
				}
				model.addAttribute("list",list);
		
		return"USR003";
	}
	@GetMapping("/setupUpdateUser/{id}")
	public String setupUpdateUser(@PathVariable String id,ModelMap model) {
		System.out.println(id);
		System.out.println("update!!");
		User user = userService.selectUserById(id);
//		System.out.println(user);
//		System.out.println(user.getId());
//		System.out.println(user.getName());
//		System.out.println(user.getPas());
//		System.out.println(user.getRole());
		
		model.addAttribute("userUpdate", user);
		return "USR002";
	}
	
	@RequestMapping(value="/UpdateUser",method=RequestMethod.POST)
	public String updateUser(@ModelAttribute ("userUpdate") @Validated UserRegisterBean bean,
			BindingResult bs,ModelMap model,HttpSession session) {
		model.addAttribute("msgUpd",false);
		List<User> list = userService.getAllUsersWithIsDeletedLessThanOne();
		User user = userService.selectUserById(bean.getId());
		if(bean.getEmail() != null) {
			for(int i = 0;i < list.size();i++) {
				if(list.get(i).getEmail().equals(bean.getEmail()) && list.get(i).getEmail() != user.getEmail()) {
					model.addAttribute("msgEmail",true);
					model.addAttribute("msgEmail", "Email already have exit!!");
					return "USR002";
				}
			}
		}
		if(bs.hasErrors()) {
			
			System.out.println("Here!!");
			return "USR002";
		}
		else {
			int i = 0;
			
			if(!bean.getC_pas().equals(bean.getPas())) {
				
				model.addAttribute("pasError","Password is not match!!");
				return "USR002";
			}
			User dto = new User();
			dto.setId(bean.getId());
			dto.setName(bean.getName());
			dto.setEmail(bean.getEmail());
			dto.setPas(bean.getPas());			
			dto.setRole(bean.getRole());
			
			System.out.println(dto.getName());
			System.out.println(dto.getEmail());
//			User dto1 = new User();
//			dto1.setId(bean.getId());
		
//			UserResponseDTO res = dao.selectOne(dto1);
//			String id = (String)session.getAttribute("id");
			i = userService.updateUser(dto);
			User res = userService.selectUserById(bean.getId());
			String id = (String)session.getAttribute("id");
			if(i > 0) {
				if(res.getId().equals(id)) {
					session.setAttribute("name",dto.getName());
					session.setAttribute("role", dto.getRole());
					
					}
				model.addAttribute("msgUpd",true);
				model.addAttribute("msgUpd", "Update Successful.");
				
			}
			else {
				
				model.addAttribute("error", "Update Failed!!");
			}
		}
		return "USR002";
	}
	@RequestMapping(value="/setupDeleteUser/{id}",method=RequestMethod.GET)
	public String deleteUser(@PathVariable String id,ModelMap model,HttpSession session) {
		
		int i = userService.deleteUser(id);
		System.out.println(id);
		if(i > 0) {
			System.out.println("Delete Successful!!");
//			if(res.getId().equals(id)) {
//			session.invalidate();
//			return "redirect:/logOut";
//			}
			return "redirect:/showUser";
		}
		else {
			session.setAttribute("error", "Delete Failed");
		}
		return "redirect:/showUser";
	}
}
