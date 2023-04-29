package app.commerce.services;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import app.commerce.dao.UserRepository;
import app.commerce.entities.User;

@Service
public class UserService  {

	 @Autowired
	 UserRepository userrepo;
	 
	 public User getLoggedUser() {
		 try {
			 Authentication loggeduser=SecurityContextHolder.getContext().getAuthentication();
			 return userrepo.getUserByemail(loggeduser.getName());
		 }catch(Exception e) {
			 e.printStackTrace();
			 return null;
		 }
	 }
	 
	 public User getByEmail(String email) {
		 try {
			 return userrepo.getUserByemail(email);
		 }catch(Exception e) {
			 e.printStackTrace();
			 return null;
		 }
	 }
	 
	 public void verify(String email) throws Exception{
		 User user=userrepo.getUserByemail(email);
		 if(user==null) {
			 throw new  Exception("User Not Found");
		 }else if(user.getEmail_verified_at()!=null) {
			 throw new  Exception("User already verified");
		 }else {
			 user.setEmail_verified_at(new Date());
			 userrepo.save(user);
		 }
	 }
	 
		private boolean isTokenExpired(User user) {
			final Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR, -1);
			return user.getPassword_token_send_ats()
					.before(cal.getTime());
		}

		
	  public void ChangerPassword(String email,String token,String password) throws Exception {
		 User user=userrepo.getUserByemail(email);
		 if(user==null) {
			 throw new  Exception("User Not Found");
		 }else {
			 String test=userrepo.GetToken(token);
			 if(test==null) {
				 throw new  Exception("Token Not Found");
			 }
			 else{
				  if(user.getPassword_token().equals(token)) {
					    if(isTokenExpired(user)) {
						   throw new Exception("Token expired");
					    }
						 user.setPassword(password);
						 user.setPassword_token(null);
						 user.setPassword_token_send_ats(null);
						 userrepo.save(user);
				  }else {
					  throw new  Exception("Token doesn\'t match");
				  }
			 }
		 }
	 
	 }
	 
	 
	 
	 
}
