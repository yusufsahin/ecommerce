package net.provera.securityserv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResetPasswordDto {
    private UUID forgotPasswordGuid;

    private String newPassword;

    private String confirmPassword;
}

