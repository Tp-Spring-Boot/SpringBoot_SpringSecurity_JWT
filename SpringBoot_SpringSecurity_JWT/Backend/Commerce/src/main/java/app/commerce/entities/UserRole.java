package app.commerce.entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.Null;

import org.hibernate.annotations.ColumnDefault;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="user_role")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class UserRole   {
	
	@EmbeddedId
	KeyRoleUserEmbed id;
	
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name="user_id")
	User user;
	
	@ManyToOne
	@MapsId("roleId")
	@JoinColumn(name="role_id")
	Role role;
	
	@ColumnDefault(value = "0")
    int status;
	
	
}
