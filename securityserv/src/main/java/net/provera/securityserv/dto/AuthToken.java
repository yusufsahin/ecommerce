package net.provera.securityserv.dto;

public class AuthToken {
    private String token;

    private String refreshToken;

    private UserDto user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public AuthToken() {
        super();
    }

    public AuthToken(String token,String refreshToken, UserDto user) {
        super();
        this.token = token;
        this.refreshToken = refreshToken;
        this.user = user;
    }


}
