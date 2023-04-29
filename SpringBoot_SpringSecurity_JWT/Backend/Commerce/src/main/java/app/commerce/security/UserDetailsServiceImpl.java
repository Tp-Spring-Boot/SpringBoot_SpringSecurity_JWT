package app.commerce.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import app.commerce.dao.UserRepository;
import app.commerce.entities.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	 @Autowired
	 private UserRepository userrep;
	 
	 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 	Optional<User> user=userrep.findByEmail(username);
		 	if(user==null) {
		 		throw new UsernameNotFoundException("could not found email : "+username);
		 	}
		 	return user.map(MyUserDetails::new).get();	
	 }
	 
}
	 
