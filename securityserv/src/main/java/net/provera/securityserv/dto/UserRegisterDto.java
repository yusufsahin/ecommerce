package net.provera.securityserv.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Schema(description = "UserRegister Data Transfer Object")
public class UserRegisterDto {

    //@Schema(description = "Id of User")
    private Long id;

   // @Schema(description = "Username of User")
    private String username;

    //@Schema(description = "password of User")
    private String password;

   // @Schema(description = "Email of User")
    private String email;
   // @Schema(description = "FirstName of User")
    private String firstname;
    //@Schema(description = "LastName of User")
    private String lastname;

}
