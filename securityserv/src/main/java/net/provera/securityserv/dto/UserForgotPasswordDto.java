package net.provera.securityserv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@ApiModel(value = "UserForgotPassword Data Transfer Object")

public class UserForgotPasswordDto {
    //@ApiModelProperty(value = "Username Or Email of User")
    private String usernameoremail ;
}
