package com.studentregistration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


import com.studentregistration.dao.UserRepository;
import com.studentregistration.dao.UserService;
import com.studentregistration.dto.User;
import com.studentregistration.dto.User;
import com.studentregistration.dto.User;

@SpringBootTest
public class TestUserService {

	@Mock
	UserRepository repo;
	
	@InjectMocks
	UserService userService;
	
	@Test
	public void getByCodeTest() {
		User setUser = new User();
		setUser.setId("USR001");
		setUser.setName("Hsu Pyae Aung");
		setUser.setEmail("hsu123@gmail.com");
		setUser.setPas("hsu111");
		setUser.setRole("User");
		setUser.setIsDeleted(0);
		when(repo.CheckUser("Hsu Pyae Aung","hsu111")).thenReturn(setUser);
		User getUser = userService.CheckUser("Hsu Pyae Aung","hsu111");
		assertEquals("USR001",getUser.getId());
		assertEquals("Hsu Pyae Aung",getUser.getName());
		assertEquals("hsu123@gmail.com",getUser.getEmail());
		assertEquals("hsu111",getUser.getPas());
		assertEquals("User",getUser.getRole());
		assertEquals(0,getUser.getIsDeleted());
	}
	@Test
	public void saveTest() {
		
		User setUser = new User();
		setUser.setId("USR001");
		setUser.setName("Hsu Pyae Aung");
		setUser.setEmail("hsu123@gmail.com");
		setUser.setPas("hsu111");
		setUser.setRole("User");
		setUser.setIsDeleted(0);
		userService.insertUser(setUser);
		verify(repo,times(1)).save(setUser);
	    
		
	}
	@Test
	public void savePasTest() {
		User setUser = new User();
		setUser.setId("USR001");
		setUser.setName("Hsu Pyae Aung");
		setUser.setEmail("hsu123@gmail.com");
		setUser.setPas("hsu111");
		setUser.setRole("User");
		setUser.setIsDeleted(0);
		
		User setUser1 = new User();
		setUser1.setId("USR001");
		setUser1.setPas("hsu111");
		when(repo.updateWithEmail("hsu111","USR001")).thenReturn(1);
		int i  = userService.insertUserWithPas("hsu111","USR001");
		assertEquals(1,i);
		
		
	}
	@Test
	public void getAllUserTest() {
		List<User> list=new ArrayList<User>();
		User setUser = new User();
		setUser.setId("USR001");
		setUser.setName("Hsu Pyae Aung");
		setUser.setEmail("hsu123@gmail.com");
		setUser.setPas("hsu111");
		setUser.setRole("User");
		setUser.setIsDeleted(0);
		
		User setUser1 = new User();
		setUser1.setId("USR002");
		setUser1.setName("Aung Aung");
		setUser1.setEmail("aa123@gmail.com");
		setUser1.setPas("aa111");
		setUser1.setRole("User");
		setUser1.setIsDeleted(0);
		
		list.add(setUser);
		list.add(setUser1);
		
		when(repo.findAllByIsDeletedLessThan(1)).thenReturn(list);
		List<User> UserList= userService.getAllUsersWithIsDeletedLessThanOne();
		assertEquals(2,UserList.size());
		verify(repo, times(1)).findAllByIsDeletedLessThan(1);
	}
	@Test
	public void getAllUserRoleTest() {
		List<User> list=new ArrayList<User>();
		User setUser = new User();
		setUser.setId("USR001");
		setUser.setName("Hsu Pyae Aung");
		setUser.setEmail("hsu123@gmail.com");
		setUser.setPas("hsu111");
		setUser.setRole("User");
		setUser.setIsDeleted(0);
		
		User setUser1 = new User();
		setUser1.setId("USR002");
		setUser1.setName("Aung Aung");
		setUser1.setEmail("aa123@gmail.com");
		setUser1.setPas("aa111");
		setUser1.setRole("User");
		setUser1.setIsDeleted(0);
		
		list.add(setUser);
		list.add(setUser1);
		
		when(repo.findByRoleAndIsDeletedLessThan("User",1)).thenReturn(list);
		List<User> UserList= userService.getAllUsersWithRoleAndIsDeletedLessThanOne();
		assertEquals(2,UserList.size());
		verify(repo, times(1)).findByRoleAndIsDeletedLessThan("User",1);
	}
	@Test
	public void getByUserTest() {
		List<User> list=new ArrayList<User>();
		User setUser = new User();
		setUser.setId("USR001");
		setUser.setName("Hsu Pyae Aung");
		setUser.setEmail("hsu123@gmail.com");
		setUser.setPas("hsu111");
		setUser.setRole("User");
		setUser.setIsDeleted(0);
		
		User setUser1 = new User();
		setUser1.setId("USR002");
		setUser1.setName("Aung Aung");
		setUser1.setEmail("aa123@gmail.com");
		setUser1.setPas("aa111");
		setUser1.setRole("User");
		setUser1.setIsDeleted(0);
		
		list.add(setUser);
		list.add(setUser1);
		
		when(repo.searchUser("USR001", "Hsu Pyae Aung")).thenReturn(list);
		List<User> getUser=userService.searchData("USR001","Hsu Pyae Aung");
		assertEquals("USR001",getUser.get(0).getId());
		assertEquals("Hsu Pyae Aung",getUser.get(0).getName());
		assertEquals("hsu123@gmail.com",getUser.get(0).getEmail());
		assertEquals("hsu111",getUser.get(0).getPas());
		assertEquals("User",getUser.get(0).getRole());
		assertEquals(0,getUser.get(0).getIsDeleted());
		
	}
	@Test
	public void updateTest() {
		User setUser = new User();
		setUser.setId("USR001");
		setUser.setName("Hsu Pyae Aung");
		setUser.setEmail("hsu123@gmail.com");
		setUser.setPas("hsu111");
		setUser.setRole("User");
		setUser.setIsDeleted(0);
		userService.updateUser(setUser);
		verify(repo,times(1)).save(setUser);
	}
	@Test
	public void getByIdTest() {
		User setUser = new User();
		setUser.setId("USR001");
		setUser.setName("Hsu Pyae Aung");
		setUser.setEmail("hsu123@gmail.com");
		setUser.setPas("hsu111");
		setUser.setRole("User");
		setUser.setIsDeleted(0);
		when(repo.getUserById("USR001")).thenReturn(setUser);
		User getUser=userService.selectUserById("USR001");
		assertEquals("USR001",getUser.getId());
		assertEquals("Hsu Pyae Aung",getUser.getName());
		assertEquals("hsu123@gmail.com",getUser.getEmail());
		assertEquals("hsu111",getUser.getPas());
		assertEquals("User",getUser.getRole());
		assertEquals(0,getUser.getIsDeleted());
	}
	@Test
	public void deleteTest() {
		userService.deleteUser("USR001");
		verify(repo,times(1)).markAsDeleted(1,"USR001");
	}
}
