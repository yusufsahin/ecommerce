package net.provera.securityserv.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTAuthorityEntity {

    private String role;
    private List<String> privileges;
}

