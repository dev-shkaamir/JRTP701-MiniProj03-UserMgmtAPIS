package com.nt.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.nt.bindings.ActivateUser;
import com.nt.bindings.LoginCredentials;
import com.nt.bindings.RecoverPassword;
import com.nt.bindings.UserAccount;
import com.nt.entity.UserMaster;
import com.nt.repository.IUserMasterRepository;
import com.nt.utils.EmailUtils;

@Service
public class UserMgmtServiceImpl implements IUserManagementService {

	
	@Autowired
	public IUserMasterRepository userMasterRepo;
	
	@Autowired
	private EmailUtils emailUtils;
	
	@Autowired
	private Environment env;
	
	@Override
	public String registerUser(UserAccount user) throws Exception{
		
		//Covert UserAccount object data to UserMaster object(Entity object) data
		UserMaster master = new UserMaster();
		BeanUtils.copyProperties(user, master);
		//Set randow password of 6 characters
		String tempPass = generateRandomPassword(6);
		master.setPassword(tempPass);
		master.setActive_sw("InActive");
		
		//Save object
		UserMaster savedMaster = userMasterRepo.save(master);
		//TODO :: send mail
		String subject = "User Registration Success";
		String body =readEmailMessageBody(env.getProperty("mailbody.registeruser.location") ,user.getName(),tempPass);
		emailUtils.sendMailMessage(user.getEmail(), subject, body);
		return savedMaster != null ? "User is registered with Id values :"+savedMaster.getUserId()+"check mail for temprory password ." : "Problem in user registraction" ;
	}

	/*
	@Override
	public String activateUserAccount(ActivateUser user) {
		//Covert ActivateUser object to Entity object (UserMaster object) data
		UserMaster master = new UserMaster();
		master.setEmail(user.getEmail());
		master.setPassword(user.getTempPassword());
		
		//Check the record available by using email and temp password
		Example<UserMaster> example = Example.of(master);
		//Since email id will always unique so it will return only 1 case.
	//(Internals) Select * from JRTP_MASTER_TABLE where email=? and tempPassword=?
		List<UserMaster> list = userMasterRepo.findAll(example);
		
		//if valid email and temp password is given the set enduser supplied 
		//real password to update the record.
		if(list != null)
		{
			//Get Entity object
			UserMaster entity = list.get(0);
			//Set the password ( Any password(new password & ConfirmPassword) you can set here because
			//Both password same validation has already happend on UI validataion.
			entity.setPassword(user.getConfirmPassword());
			
			//change the user account status to active
			entity.setActive_sw("Active");
			
			//Update the object
			UserMaster updatedEntity = userMasterRepo.save(entity);
			
			return "User  activated with new password";
		}
		return "User is not found for activation";
	}
*/
	
	//If you don't want to use Example interface of Spring Boot then you can 
	//  your own custum method in Repository interface.
	
	@Override
	public String activateUserAccount(ActivateUser user) {
		//use find by method declared in Repository interface.
		UserMaster entity = userMasterRepo.findByEmailAndPassword(user.getEmail(), user.getTempPassword());
		if(entity == null)
		{
			return "User not found for activation";
		}else
		{
            entity.setPassword(user.getConfirmPassword());
			//change the user account status to active
			entity.setActive_sw("Active");
			//Update the object
			UserMaster updatedEntity = userMasterRepo.save(entity);
			
			return "User  activated with new password";
		}
	}
	
	@Override
	public String login(LoginCredentials credential) {
		//Convert LoginCredentials object to UserMaster object
		UserMaster master = new UserMaster();
		BeanUtils.copyProperties(credential, master);
		
		//Prepared Example object
		Example<UserMaster> example = Example.of(master);
		List<UserMaster> listEntities = userMasterRepo.findAll(example);
		if(listEntities.size() == 0)
		{
			return "Invalid Credentials";
		}else {
			UserMaster entity = listEntities.get(0);
			if(entity.getActive_sw().equalsIgnoreCase("Active"))
			{
				return "Valid Credential and Login Successful";
			}else
			{
				return "User Account is not Active";
			}
		}

	}

	@Override
	public List<UserAccount> listUsers() {
		
		//Load all entites
		List<UserMaster> listEntities = userMasterRepo.findAll();
		
		
		//Convert all entites to UserAccount
		List<UserAccount> userList = new ArrayList<>();
		listEntities.forEach(
				entity->{
					UserAccount user = new UserAccount();
					BeanUtils.copyProperties(entity, user);
					userList.add(user);
				}
				);
	
		
		/*
		//Using Stream API's
		//Load all entities and convert to UserAccount object.
		List<UserAccount> userList = userMasterRepo.findAll().stream().map(
				entity->{
					UserAccount user = new UserAccount();
					BeanUtils.copyProperties(entity, user);
					return user;
				}
				).toList();
				
		*/
		return userList;
		
	}

	@Override
	public UserAccount showUserByUserId(Integer id) {
		//Load the user by user id
		Optional<UserMaster> opt = userMasterRepo.findById(id);
		UserAccount account = null;
		if(opt.isPresent())
		{
			 account = new UserAccount();
			BeanUtils.copyProperties(opt.get(), account);
			return account;
		}
		return account;
	}

	@Override
	public UserAccount showUserByEmailAndName(String email, String name) {
		
		//User Custom findBy(-) method defined in Repository
		UserMaster master = userMasterRepo.findByNameAndEmail(name, email);
		UserAccount userAccount = null;
		if(master != null)
		{
			userAccount = new UserAccount();
			BeanUtils.copyProperties(master, userAccount);
		}
		return userAccount;
	}

	@Override
	public String updateUser(UserAccount user) {
		
		Optional<UserMaster> opt = userMasterRepo.findById(user.getUserId());
		if(opt.isPresent())
		{
			//Get Entityes object
			UserMaster master = opt.get();
			BeanUtils.copyProperties(user, master);
			userMasterRepo.save(master);
			return "User Details are updated";
		}else
		{
			return "User Details not found to update";
		}
	}

	@Override
	public String deleteUserById(Integer id) {
		//Load the object
		Optional<UserMaster> opt = userMasterRepo.findById(id);
		if(opt.isPresent())
		{
			
			userMasterRepo.deleteById(id);
			return "User has been delete ";
		}
		return "User not found for deletion";
	}

	@Override
	public String changeUserStatus(Integer id, String status) {
		//Load the object
		Optional<UserMaster> opt = userMasterRepo.findById(id);
		if(opt.isPresent())
		{
			//Get entity object
			UserMaster master = opt.get();
			//changes the status
			master.setActive_sw(status);
			//Update the object
			userMasterRepo.save(master);
			
			return "User status  changed";
		}
		return "User not found for chaning the status";
	}

	@Override //Forget password
	public String recoredPassword(RecoverPassword recover) throws Exception {
		//get UserMaster (Entity object) by name , email
		UserMaster master = userMasterRepo.findByNameAndEmail(recover.getName(), recover.getEmail());
		if(master != null)
		{
			String pwd = master.getPassword();
			// send the recoverd password to email account
			String subject = "Mail for password recovery";
			String mailBody = readEmailMessageBody(env.getProperty("mailbody.recoverpwd.location"),subject , pwd);
			emailUtils.sendMailMessage(recover.getEmail(), subject, mailBody);
			return pwd+" mail is send having the recoverd password";
		}
		return "User and Email not found";
		
	}
	//helper method for same class
	private static String generateRandomPassword(int length) {
	    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    Random random = new Random();
	    StringBuilder randomPassword = new StringBuilder(length);

	    for (int i = 0; i < length; i++) {
	    	randomPassword.append(characters.charAt(random.nextInt(characters.length())));
	    }

	    return randomPassword.toString();
	}
	
	
	private String readEmailMessageBody(String fileName,String fullName , String password ) throws Exception
	{
		String mailBody = null;
		String url = "";
		try(FileReader reader = new FileReader(fileName);
			BufferedReader br = new BufferedReader(reader)) 
		{
			//Read file content to StringBuffer object line by line
			StringBuffer buffer = new StringBuffer();
			String line = null;
			do {
				line = br.readLine();
				buffer.append(line);
			}while(line != null);
			mailBody = buffer.toString();
			mailBody.replace("{FULL-NAME}", fullName);
			mailBody.replace("{PWD}", password);
			mailBody.replace("{URL}", url);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		
		return mailBody;
	}

}
