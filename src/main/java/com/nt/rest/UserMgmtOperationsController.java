package com.nt.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.bindings.UserAccount;
import com.nt.service.IUserManagementService;

//Rest Controller
@RestController
@RequestMapping("/user-api")
public class UserMgmtOperationsController {

	@Autowired
	private IUserManagementService userService;

	public ResponseEntity<String> saveUser(@RequestBody UserAccount account) {
		try {
			String resultMsg = userService.registerUser(account);
			return new ResponseEntity<String>(resultMsg, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
