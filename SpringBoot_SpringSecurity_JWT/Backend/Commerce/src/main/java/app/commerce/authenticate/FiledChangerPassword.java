package app.commerce.authenticate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiledChangerPassword {
  private String token;
  private String email;
  private String password;
}
