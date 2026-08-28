package com.nt.bindings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor  
public class ActivateUser {
   private String email;
   private String tempPassword;
   private String newPassword;
   private String confirmPassword;
   public String getEmail() {
	return email;
   }
   public void setEmail(String email) {
	this.email = email;
   }
   public String getTempPassword() {
	return tempPassword;
   }
   public void setTempPassword(String tempPassword) {
	this.tempPassword = tempPassword;
   }
   public String getNewPassword() {
	return newPassword;
   }
   public void setNewPassword(String newPassword) {
	this.newPassword = newPassword;
   }
   public String getConfirmPassword() {
	return confirmPassword;
   }
   public void setConfirmPassword(String confirmPassword) {
	this.confirmPassword = confirmPassword;
   }
   
}
