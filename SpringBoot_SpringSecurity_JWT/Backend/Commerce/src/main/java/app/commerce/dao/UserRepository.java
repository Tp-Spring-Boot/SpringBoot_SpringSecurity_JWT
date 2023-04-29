package app.commerce.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import app.commerce.entities.User;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value="select * from user where cin=:cin",nativeQuery=true)
	User getUserByCin(String cin);
	
	@Query(value="select * from user where email=:email",nativeQuery=true)
	User getUserByemail(String email);
	
	@Query(value="select * from user where password_token=:token",nativeQuery=true)
	String GetToken(String token);
	
	Optional<User> findByEmail(String email);
	
	//User findByCin(String cin);
	
	//Optional<User> findByCin(String cin);
	
	
}
