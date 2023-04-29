package app.commerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import app.commerce.entities.UserRole;

public interface RoleUserRepository extends JpaRepository<UserRole, Long> {

}
