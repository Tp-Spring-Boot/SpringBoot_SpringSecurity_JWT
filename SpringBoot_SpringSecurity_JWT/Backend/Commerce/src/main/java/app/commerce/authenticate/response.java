package app.commerce.authenticate;

import org.springframework.security.core.userdetails.UserDetails;

import app.commerce.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class response {
	    
	    private String token;
	    private User user;
	    
}
