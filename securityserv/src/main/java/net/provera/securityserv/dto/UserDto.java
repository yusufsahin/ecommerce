package net.provera.securityserv.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Schema(description = "Workitem Data Transfer Object")
public class UserDto {

    //@Schema(description = "Id of User")
    private Long id;

    @NotNull
    //@Schema(description = "Username of User")
    private String username;

    //@Schema(description = "Email of User")
    private String email;
   // @Schema(description = "FirstName of User")
    private String firstname;
    //@Schema(description = "LastName of User")
    private String lastname;
    //@Schema(description = "PhoneNum of User")
    private String phonenum;
    //@Schema(description = "Picture of User")
    private byte[] picture;


}
