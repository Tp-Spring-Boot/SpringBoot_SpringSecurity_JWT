package app.commerce.RestController;
import java.io.UnsupportedEncodingException;

import java.util.Date;
import java.util.List;


import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.commerce.Exception.AccountNotActived;
import app.commerce.Exception.CinExiste;
import app.commerce.Exception.EmailExiste;
import app.commerce.authenticate.Credentials;
import app.commerce.authenticate.FiledChangerPassword;
import app.commerce.authenticate.response;
import app.commerce.dao.RoleRepository;
import app.commerce.dao.UserRepository;
import app.commerce.entities.User;
import app.commerce.jwt.JwtTokenUtil;
import app.commerce.security.SecurityConfig;
import app.commerce.security.UserDetailsServiceImpl;
import app.commerce.services.Mail;
import app.commerce.services.UserService;
import net.bytebuddy.utility.RandomString;

@RestController
public class AuthController {
	
	@Autowired
	UserRepository usertest;
	
	@Autowired
	RoleRepository rolerep;

    @Autowired
	private SecurityConfig SecurityConfig;

    @Autowired
	UserDetailsServiceImpl userservice;
    
    @Autowired
    UserService user_service;
    
    @Autowired
	private JwtTokenUtil jwtTokenUtil;
       
    @Autowired
	AuthenticationManager authenticationManager; 
    
    @Autowired
    Mail sendmail;
 
	@PostMapping("/signup")
	public ResponseEntity<?> SignUp(@RequestBody User u) throws EmailExiste,CinExiste{
		User n=new User();
		
		if(usertest.getUserByemail(u.getEmail()) != null) {
			throw new EmailExiste("Email already existe");
		}
		
		if(usertest.getUserByCin(u.getCin())!=null) {
			throw new CinExiste("Cin already existe");
		}
		
		n.setFirstName(u.getFirstName());
		n.setLastName(u.getLastName());
		n.setRoles(u.getRoles());
		n.setBirth_day(u.getBirth_day());
		n.setCin(u.getCin());
		n.setEmail(u.getEmail());
		//n.setEmail_verified_at(null);
		
		if(u.getPhoto()==null) {
			n.setPhoto(u.getFirstName().charAt(0)+""+u.getLastName().charAt(0));
		}else {
			n.setPhoto(u.getPhoto());
		}
		n.setPassword(SecurityConfig.passwordEncoder().encode(u.getPassword()));
		   try {
				sendmail.sendVerificationEmail(u);
		   }catch(MessagingException e) {
			   return new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
		   }catch(UnsupportedEncodingException e) {
			   return new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
		   }
		   
		usertest.save(n);
		return new ResponseEntity<>(n,HttpStatus.OK);
	}
	  
	   @PostMapping("/login")
	   public ResponseEntity<?> authenticateUser(@RequestBody Credentials auth) throws Exception,AccountNotActived {
		    try {
		    	 if (usertest.getUserByemail(auth.getEmail())==null) {
		    		 return new ResponseEntity<String>("User not found",HttpStatus.CONFLICT);
				 }
		    	Authentication authsuser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword()));
		    }catch(BadCredentialsException e) {
		    	return new ResponseEntity<String>("Incorrect email or password",HttpStatus.CONFLICT);
		    }
			
		    UserDetails user_det=userservice.loadUserByUsername(auth.getEmail());
		    User user=user_service.getByEmail(auth.getEmail());
		    if(user.getEmail_verified_at()==null) {
		    	throw new AccountNotActived("Account not actived");
		    }
		    String token=jwtTokenUtil.generateToken(user_det);
		    response res=new response(token,user);
	        return  ResponseEntity.ok().body(res);
	    }
	
	@GetMapping("/getUsers")
	public List<User> getUers(){
		return  usertest.findAll();
	}
	
	@PostMapping("/verifyAccount")
	public ResponseEntity<?> verify(@RequestParam("email") String email) throws Exception{
		try {
			user_service.verify(email);
			return new ResponseEntity<String>("Email Verified ",HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
		}
	}
	
	@SuppressWarnings("finally")
	@PostMapping("/FotgotPassword")
	public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) throws Exception{
		User user=usertest.getUserByemail(email);
		if(user!=null) {
			String token=RandomString.make(8);
			user.setPassword_token(token);
			user.setPassword_token_send_ats(new Date());
			usertest.save(user);
			 try {
				 sendmail.ForgotPassword(token, user);
				   return new ResponseEntity<String>("We have sent a reset password token to your email. Please check.",HttpStatus.OK);
			   }catch(MessagingException e) {
				   return new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
			   }catch(UnsupportedEncodingException e) {
				   return new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
			   }
		}else {
		      return new ResponseEntity<String>("User Not Found",HttpStatus.CONFLICT);
		}
	   }
	
	@PostMapping("/ChangerPassword")
	public ResponseEntity<?> changerPassword(@RequestBody FiledChangerPassword data){
		 String PasswordHasher= SecurityConfig.passwordEncoder().encode(data.getPassword());
		 try {
			 user_service.ChangerPassword(data.getEmail(), data.getToken(), PasswordHasher);
			 return new ResponseEntity<String>("Password changed with success",HttpStatus.OK);
		 }catch(Exception e) {
			 return new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
		 }
	}

}
