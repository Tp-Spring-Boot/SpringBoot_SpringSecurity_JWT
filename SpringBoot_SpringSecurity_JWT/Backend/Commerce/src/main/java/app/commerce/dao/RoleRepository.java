package app.commerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import app.commerce.entities.Role;


@EnableJpaRepositories
public interface RoleRepository extends JpaRepository<Role, Long> {
   
}
