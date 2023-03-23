package com.studentregistration;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.Mockito.verifyZeroInteractions;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;


import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;


import com.studentregistration.controller.UserController;
import com.studentregistration.dao.UserRepository;
import com.studentregistration.dao.UserService;
import com.studentregistration.dto.User;
import com.studentregistration.model.UserBean;
import com.studentregistration.model.UserLoginBean;
import com.studentregistration.model.UserRegisterBean;
import com.studentregistration.model.UserSearchBean;

import junit.framework.Assert;

import org.junit.Before;


@SpringBootTest
@AutoConfigureMockMvc
public class TestUserController {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	UserService userService;
	
	@MockBean
	UserRepository repo;
	
	 @InjectMocks
	    private UserController userLoginController;
	    
	    @Mock
	    private UserLoginBean bean;
	    
	    @Mock
	    private BindingResult bs;
	    
	    @Mock
	    private ModelMap model;
	    
	    @Mock
	    private HttpSession session;
	    
	    @Mock
	    private ModelMap modelMap;
	    
	    private UserRegisterBean userRegisterBean;
	    
	    @Mock
	    private BindingResult bindingResult;
	    
	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }
	    
	    
	@Test
	public void testsetupLogin() throws Exception {
		this.mockMvc.perform(get("/"))
		.andExpect(status().isOk())
		.andExpect(view().name("LGN001"))
		.andExpect(model().attributeExists("userLogin"));
	}
	@Test
	public void testsetupLogOut() throws Exception {
		this.mockMvc.perform(get("/logOut"))
		.andExpect(status().isOk())
		.andExpect(view().name("LGN001"))
		.andExpect(model().attributeExists("userLogin"));
	}
	@Test
	public void testLoginvalidate() throws Exception {
		this.mockMvc.perform(post("/userLogin"))
		.andExpect(status().isOk())
		.andExpect(view().name("LGN001"));	
	}
	
	//testing addbook method when validated pass
	
	 @Test
	    public void testUserLoginWithValidCredentials() {
	        // given
	        User user = new User();
	        user.setName("username");
	        user.setPas("password");
	        User user1 = new User();
	        user1.setId("USR001");
	        user1.setName("username");
	        user1.setRole("user");
	        when(userService.CheckUser(user.getName(), user.getPas())).thenReturn(user1);
	        when(bs.hasErrors()).thenReturn(false);
	        when(bean.getName()).thenReturn(user.getName());
	        when(bean.getPas()).thenReturn(user.getPas());
	        
	        // when
	        String result = userLoginController.UserLogin(bean, bs, model, session);
	        
	        // then
	        
	        verify(session).setAttribute(eq("id"), eq(user1.getId()));
	        verify(session).setAttribute(eq("name"), eq(user1.getName()));
	        verify(session).setAttribute(eq("role"), eq(user1.getRole()));
	        verify(session).setAttribute(eq("value"), eq(true));
	        assertEquals("MNU001", result);
	    }

	    @Test
	    public void testUserLoginWithInvalidCredentials() {
	        // given
	        User user = new User();
	        user.setName("username");
	        user.setPas("password");
	        when(userService.CheckUser(user.getName(), user.getPas())).thenReturn(null);
	        when(bs.hasErrors()).thenReturn(false);
	        when(bean.getName()).thenReturn(user.getName());
	        when(bean.getPas()).thenReturn(user.getPas());
	        
	        // when
	        String result = userLoginController.UserLogin(bean, bs, model, session);
	        
	        // then
	        verify(model).addAttribute(eq("loginError"), eq("Login Failed!"));
	        assertEquals("LGN001", result);
	    }

	    @Test
	    public void testUserLoginWithBindingErrors() {
	        // given
	        when(bs.hasErrors()).thenReturn(true);
	        when(bs.getAllErrors()).thenReturn(Arrays.asList(new ObjectError("userLogin", "Validation failed")));
	        
	        // when
	        String result = userLoginController.UserLogin(bean, bs, model, session);
	        
	        // then
	        assertEquals("LGN001", result);
	    }
	
	@Test
	public void testsetupEmail() throws Exception {
		this.mockMvc.perform(get("/userEmail"))
		.andExpect(status().isOk())
		.andExpect(view().name("LGN002"))
		.andExpect(model().attributeExists("userEmail"));
	}
	@Test
	public void testUserEmailWithValidEmail() {
	    // given
	    UserRegisterBean bean = new UserRegisterBean();
	    bean.setEmail("user@example.com");
	    List<User> list = new ArrayList<>();
	    User user = new User();
	    user.setId("USR001");
	    user.setEmail("user@example.com");
	    list.add(user);
	    when(userService.getAllUsersWithIsDeletedLessThanOne()).thenReturn(list);
	    
	    // when
	    String result = userLoginController.UserEmail(bean, model, session);
	    
	    // then
	    verify(session).setAttribute(eq("userId"), eq("USR001"));
	    assertEquals("redirect:/pasCreate", result);
	}

	@Test
	public void testUserEmailWithInvalidEmail() {
	    // given
	    UserRegisterBean bean = new UserRegisterBean();
	    bean.setEmail("user@example.com");
	    List<User> list = new ArrayList<>();
	    User user = new User();
	    user.setId("USR001");
	    user.setEmail("otheruser@example.com");
	    list.add(user);
	    when(userService.getAllUsersWithIsDeletedLessThanOne()).thenReturn(list);
	    
	    // when
	    String result = userLoginController.UserEmail(bean, model, session);
	    
	    // then
	    verify(model).addAttribute(eq("msg"), eq(true));
	    verify(model).addAttribute(eq("msg"), eq("Invalid Email!!"));
	    assertEquals("LGN002", result);
	}
	@Test
	public void testsetupPas() throws Exception {
		this.mockMvc.perform(get("/pasCreate"))
		.andExpect(status().isOk())
		.andExpect(view().name("LGN003"))
		.andExpect(model().attributeExists("userPas"));
	}
	public void testUserPas() {
        // Create a UserRegisterBean with matching passwords
        UserRegisterBean bean = new UserRegisterBean();
        bean.setPas("password");
        bean.setC_pas("password");
        
        // Set the session attribute for user ID
        String userId = "1234";
        when(session.getAttribute("userId")).thenReturn(userId);
        
        // Set up the UserService mock to return a User object
        User user = new User();
        when(userService.insertUserWithPas(anyString(), anyString())).thenReturn(1);
        
        // Call the controller method and verify the result
        String result = userLoginController.UserPas(bean, model, session);
        verify(session).getAttribute("userId");
        verify(userService).insertUserWithPas(eq("password"), eq(userId));
        assertEquals("redirect:/", result);
    }
    
    @Test
    public void testUserPasWithMismatchedPasswords() {
        // Create a UserRegisterBean with mismatched passwords
        UserRegisterBean bean = new UserRegisterBean();
        bean.setPas("password");
        bean.setC_pas("different_password");
        
        // Call the controller method and verify the result
        String result = userLoginController.UserPas(bean, model, session);
        verifyZeroInteractions(session, userService);
        assertEquals("LGN003", result);
        
    }
    
    @Test
    public void testUserPasWithNullUser() {
        // Create a UserRegisterBean with matching passwords
        UserRegisterBean bean = new UserRegisterBean();
        bean.setPas("password");
        bean.setC_pas("password");
        
        // Set the session attribute for user ID
        String userId = "1234";
        when(session.getAttribute("userId")).thenReturn(userId);
        
        // Set up the UserService mock to return null
        when(userService.insertUserWithPas(anyString(), anyString())).thenReturn(0);
        
        // Call the controller method and verify the result
        String result = userLoginController.UserPas(bean, model, session);
        verify(session).getAttribute("userId");
        verify(userService).insertUserWithPas(eq("password"), eq(userId));
        assertEquals("LGN003", result);
    }
    @Test
    public void testUSR001() throws Exception {
        // Mock dependencies
        String userId = "testUserId";
        when(userService.generateUserId()).thenReturn(userId);
        when(session.getAttribute("id")).thenReturn(userId);

        // Call method
        ModelAndView mav = userLoginController.USR001(new ModelMap(), session);

        // Verify session attributes were set correctly
        verify(session).setAttribute("id", userId);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String expectedDate = formatter.format(new Date());
        verify(session).setAttribute("date", expectedDate);

        // Verify ModelAndView was created correctly
        this.mockMvc.perform(get("/setUpUserRegister"))
		.andExpect(status().isOk())
		.andExpect(view().name("USR001"))
		.andExpect(model().attributeExists("userRegister"));
    }
    @Before
    public void setUp1() {
        userRegisterBean = new UserRegisterBean();
        userRegisterBean.setName("John Doe");
        userRegisterBean.setEmail("johndoe@example.com");
        userRegisterBean.setPas("1234");
        userRegisterBean.setC_pas("1234");
        userRegisterBean.setRole("USER");
    }
    
    @Test
    public void testUserRegister_SuccessfulRegistration() {
        // Arrange
        String userId = "1234";
        userRegisterBean = new UserRegisterBean();
        userRegisterBean.setName("John Doe");
        userRegisterBean.setEmail("johndoe@example.com");
        userRegisterBean.setPas("1234");
        userRegisterBean.setC_pas("1234");
        userRegisterBean.setRole("USER");
        User user = new User();
        user.setId(userId);
        user.setName(userRegisterBean.getName());
        user.setEmail(userRegisterBean.getEmail());
        user.setPas(userRegisterBean.getPas());
        user.setRole(userRegisterBean.getRole());
        when(userService.generateUserId()).thenReturn(userId);
        when(userService.getAllUsersWithIsDeletedLessThanOne()).thenReturn(new ArrayList<User>());
        when(userService.insertUser(any(User.class))).thenReturn(user);
        when(bindingResult.hasErrors()).thenReturn(false);
        
        // Act
        String result = userLoginController.UserRegister(userId, userRegisterBean, bindingResult, model, session);
        
        // Assert
        verify(session).setAttribute("msgReg", true);
        verify(session).setAttribute("msgReg", "Registration Successful.");
        assertEquals("redirect:setUpUserRegister", result);
    }
    
    @Test
    public void testUserRegister_FailedRegistration_EmailAlreadyExists() {
        // Arrange
        String userId = "1234";
        userRegisterBean = new UserRegisterBean();
        userRegisterBean.setName("John Doe");
        userRegisterBean.setEmail("johndoe@example.com");
        userRegisterBean.setPas("1234");
        userRegisterBean.setC_pas("1234");
        userRegisterBean.setRole("USER");
        User existingUser = new User();
        existingUser.setEmail(userRegisterBean.getEmail());
        when(userService.generateUserId()).thenReturn(userId);
        when(userService.getAllUsersWithIsDeletedLessThanOne()).thenReturn(Arrays.asList(existingUser));
        when(bindingResult.hasErrors()).thenReturn(false);
        
        // Act
        String result = userLoginController.UserRegister(userId, userRegisterBean, bindingResult, model, session);
        
        // Assert
        verify(model).addAttribute("msgEmail", true);
        verify(model).addAttribute("msgEmail", "Email already have exit!!");
        assertEquals("USR001", result);
    }
    
    @Test
    public void testUserRegister_FailedRegistration_PasswordNotMatch() {
        // Arrange
    	  userRegisterBean = new UserRegisterBean();
          userRegisterBean.setName("John Doe");
          userRegisterBean.setEmail("johndoe@example.com");
          userRegisterBean.setPas("1234");
          userRegisterBean.setC_pas("1234");
          userRegisterBean.setRole("USER");
        userRegisterBean.setC_pas("5678");
        
        when(bindingResult.hasErrors()).thenReturn(false);
        
        // Act
        String result = userLoginController.UserRegister("1234", userRegisterBean, bindingResult, model, session);
        
        // Assert
        verify(model).addAttribute("pasError", "Password is not match!");
        assertEquals("USR001", result);
    }
    
    @Test
    public void testUserRegister_FailedRegistration_ValidationErrors() {
        // Arrange
    	  userRegisterBean = new UserRegisterBean();
          userRegisterBean.setName("John Doe");
          userRegisterBean.setEmail("johndoe@example.com");
          userRegisterBean.setPas("1234");
          userRegisterBean.setC_pas("1234");
          userRegisterBean.setRole("USER");
        when(bindingResult.hasErrors()).thenReturn(true);
        
        // Act
        String result = userLoginController.UserRegister("1234", userRegisterBean, bindingResult, model, session);
        
        // Assert
        assertEquals("USR001", result);
    }
    @Test
    public void testSetupUserSearch() throws Exception {
        // mock the userService.getAllUsersWithRoleAndIsDeletedLessThanOne() method to return a list of users
        ArrayList<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setId("1");
        user1.setName("John");
        user1.setEmail("john@example.com");
        user1.setRole("user");
        user1.setIsDeleted(0);
        userList.add(user1);
        User user2 = new User();
        user2.setId("2");
        user2.setName("Jane");
        user2.setEmail("jane@example.com");
        user2.setRole("admin");
        user2.setIsDeleted(0);
        userList.add(user2);
        when(userService.getAllUsersWithRoleAndIsDeletedLessThanOne()).thenReturn(userList);
        
        this.mockMvc.perform(get("/showUser"))
		.andExpect(status().isOk())
		.andExpect(view().name("USR003"))
		.andExpect(model().attributeExists("search"));
        
        // assert that the userService.getAllUsersWithRoleAndIsDeletedLessThanOne() method was called once
        verify(userService, times(1)).getAllUsersWithRoleAndIsDeletedLessThanOne();
        
    }
    @Test
    public void testSearchUser() {
        // Setup
        UserSearchBean search = new UserSearchBean();
        search.setId("123");
        search.setName("John");
        User dto = new User();
        dto.setId(search.getId());
        dto.setName(search.getName());
        List<User> userList = new ArrayList<>();
        userList.add(dto);
        Mockito.when(userService.searchData(dto.getId(), dto.getName())).thenReturn(userList);

        // Execute
        String viewName = userLoginController.searchUser(search, modelMap, session);

        // Verify
        assertEquals("USR003", viewName);
        verify(modelMap).addAttribute("list", userList);
        verify(modelMap, never()).addAttribute("msgNotFound", true);
    }

    @Test
    public void testSearchUser_notFound() {
        // Setup
        UserSearchBean search = new UserSearchBean();
        search.setId("123");
        search.setName("John");
        User dto = new User();
        dto.setId(search.getId());
        dto.setName(search.getName());
        List<User> userList = new ArrayList<>();
        Mockito.when(userService.searchData(dto.getId(), dto.getName())).thenReturn(userList);

        // Execute
        String viewName = userLoginController.searchUser(search, modelMap, session);

        // Verify
        assertEquals("USR003", viewName);
        //verify(modelMap, never()).addAttribute("list", userList);
        verify(modelMap).addAttribute("msgNotFound", true);
        verify(modelMap).addAttribute("searchError", "User Data not found!!");
    
}
    @Test
	public void testupdateView() throws Exception {
		 this.mockMvc.perform(get("/setupUpdateUser/USR001"))
	        .andExpect(status().isOk())
	        .andExpect(view().name("USR002"));
	}
    public void testupdateUserOk() throws Exception {
		UserRegisterBean u = new UserRegisterBean();
	        u.setName("John Doe");
	        u.setEmail("johndoe@example.com");
	        u.setPas("1234");
	        u.setC_pas("1234");
	        u.setRole("USER");
		this.mockMvc.perform(post("/updatebook").flashAttr("userUpdate", u))
		.andExpect(status().isOk())
		.andExpect(view().name("USR002"));	
	}
    @Test
	public void testdeleteUser() throws Exception {
		this.mockMvc.perform(get("/setupDeleteUser/USR001"))
		.andExpect(status().is(302))
		.andExpect(redirectedUrl("/showUser"));	
	}
    
}
