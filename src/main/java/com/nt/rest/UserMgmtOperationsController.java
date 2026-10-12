package com.nt.rest;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.bindings.ActivateUser;
import com.nt.bindings.LoginCredentials;
import com.nt.bindings.RecoverPassword;
import com.nt.bindings.UserAccount;
import com.nt.service.IUserManagementService;

//Rest Controller
@RestController
@RequestMapping("/user-api")
public class UserMgmtOperationsController {

	@Autowired
	private IUserManagementService userService;

	
	@PostMapping("/save")
	public ResponseEntity<String> saveUser(@RequestBody UserAccount account) {
		try {
			String resultMsg = userService.registerUser(account);
			return new ResponseEntity<String>(resultMsg, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/activate")
	public ResponseEntity<String> activeUser(@RequestBody ActivateUser user)
	{
		
		try {
			//Service
			String resutlMsg = userService.activateUserAccount(user);
			return new ResponseEntity<String>(resutlMsg, HttpStatus.CREATED);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<String> performLogin(@RequestBody LoginCredentials credential)
	{
		
		try {
			//Service
			String resutlMsg = userService.login(credential);
			return new ResponseEntity<String>(resutlMsg, HttpStatus.OK);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/report")
	public ResponseEntity<?> showUsers()
	{
		
		try {
			//Service
			List<UserAccount> list = userService.listUsers();
			
			return new ResponseEntity<List<UserAccount>>(list, HttpStatus.OK);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/find/{id}")
	public ResponseEntity<?> showUserById(@PathVariable Integer id)
	{
		try {
			//Service
			UserAccount account = userService.showUserByUserId(id);
			return new ResponseEntity<UserAccount>(account, HttpStatus.OK);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/find/{email}/{name}")
	public ResponseEntity<?> showUserByEmailAndName(@PathVariable String email ,@PathVariable String name )
	{
		try {
			//Service
			UserAccount account = userService.showUserByEmailAndName(email, name);
			return new ResponseEntity<UserAccount>(account, HttpStatus.OK);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateUserDetails(@RequestBody UserAccount account)
	{
		try {
			//Service
			String resultMsg = userService.updateUser(account);
			return new ResponseEntity<String>(resultMsg, HttpStatus.OK);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUserById(@PathVariable Integer id)
	{
		try {
			//Service
			String resultMsg = userService.deleteUserById(id);
			return new ResponseEntity<String>(resultMsg, HttpStatus.OK);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PutMapping("/changeStatus/{id}/{status}")
	public ResponseEntity<String> changeStatus(@PathVariable Integer id , @PathVariable String status)
	{
		try {
			//Service
			String resultMsg = userService.changeUserStatus(id, status);
			return new ResponseEntity<String>(resultMsg, HttpStatus.OK);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/recoverPassword")
	public ResponseEntity<String> recoverPassword(@RequestBody RecoverPassword recover)
	{
		try {
			//Service
			String resultMsg = userService.recoredPassword(recover);
			return new ResponseEntity<String>(resultMsg, HttpStatus.OK);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
