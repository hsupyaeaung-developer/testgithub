package com.studentregistration.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentregistration.dto.User;

@Service
public class UserService {

	@Autowired 
	UserRepository userRepository;
	public User CheckUser(String name,String pas) {
		User user = new User();
		user = userRepository.CheckUser(name, pas);
		return user;
	}
	public User insertUser(User user) 
	{ 
		User user1 = new User();
		user1 = userRepository.save(user);
		return user1;
	}
	public int insertUserWithPas(String pas,String id) 
	{ 
		int i = 0;
		userRepository.updateWithEmail(pas, id);
		i = 1;
		return i;
	}
    public String generateUserId() {
        int count = userRepository.countUsers() + 1;
        String userId = null;
        
        if (count < 10) {
            userId = "USR00" + count;
        } else if (count >= 10 && count < 100) {
            userId = "USR0" + count;
        } else if (count == 100) {
            userId = "USR" + count;
        }
        
        return userId;
    }

    public List<User> getAllUsersWithIsDeletedLessThanOne() {
        return userRepository.findAllByIsDeletedLessThan(1);
    }
    public List<User> getAllUsersWithRoleAndIsDeletedLessThanOne() {
    	String role = "User";
        return userRepository.findByRoleAndIsDeletedLessThan(role,1);
    }
    
    public List<User> searchData(String id,String name) 
    { 
    	List<User> list = (List<User>) userRepository.searchUser(id,name);
    	return list;
    } 
//    public Optional<User> getUserById(String id) {
//    	return userRepository.findById(id);
//    	}
    public User selectUserById(String id) {
    	User user = userRepository.getUserById(id);
    	return user;
    	}
    public int deleteUser(String id) 
    { 
    	int result = 0;
    	userRepository.markAsDeleted(1,id);
    	result = 1;
    	return result;
    }
    public int updateUser(User dto) 
    { 
    int i = 0;
    userRepository.save(dto); 
    i = 1;
    return i;
    } 
}
