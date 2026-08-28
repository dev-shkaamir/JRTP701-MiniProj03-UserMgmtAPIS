package com.nt.service;

import java.util.List;

import com.nt.bindings.ActivateUser;
import com.nt.bindings.LoginCredentials;
import com.nt.bindings.RecoverPassword;
import com.nt.bindings.UserAccount;

public interface IUserManagementService {
	
	public String registerUser(UserAccount user) throws Exception ;
	public String activateUserAccount(ActivateUser user);
	public String login(LoginCredentials credential);
	public List<UserAccount> listUsers();
	public UserAccount showUserByUserId(Integer id);
	public UserAccount showUserByEmailAndName(String email , String name);
	public String updateUser(UserAccount user); 
	public String deleteUserById(Integer id);
	public String changeUserStatus(Integer id , String status);
	public String recoredPassword(RecoverPassword recover) throws Exception;//Forgot password.

}
