package app.commerce.authenticate;


import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
 private String email;
 private String password;
}
