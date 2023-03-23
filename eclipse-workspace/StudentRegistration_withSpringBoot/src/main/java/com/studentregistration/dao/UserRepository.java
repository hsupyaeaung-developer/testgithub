package com.studentregistration.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.studentregistration.dto.User;


public interface UserRepository extends JpaRepository<User, String> 
{
	@Query("SELECT u FROM User u WHERE u.isDeleted = 0 AND (u.name = ?1 AND u.pas = ?2)")
	public User CheckUser (String name,String pas);
	
	 @Query(value = "SELECT COUNT(*) FROM User")
	 int countUsers();
	 
	 @Query("SELECT u FROM User u WHERE u.role = 'User' AND u.isDeleted = 0 AND (u.id = ?1 OR u.name = ?2)")
	 List<User> searchUser(String id, String name);
	 
	  @Modifying
	  @Transactional
	  @Query("UPDATE User u SET u.isDeleted = ?1 WHERE u.id = ?2")
	  void markAsDeleted(int isDeleted,String id);
	 
	 List<User> findAllByIsDeletedLessThan(int isDeleted);
	 
	 List<User> findByRoleAndIsDeletedLessThan(String role,int isDeleted);
	 
	 @Query("SELECT u FROM User u WHERE u.id=?1")
	 public User getUserById(String id);
	 
	 @Modifying
	 @Transactional
	 @Query("UPDATE User u SET u.pas=?1 WHERE u.id = ?2")
	 int updateWithEmail(String pas,String id);
}

