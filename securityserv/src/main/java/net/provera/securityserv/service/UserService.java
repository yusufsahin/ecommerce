package net.provera.securityserv.service;



import net.provera.securityserv.dto.UserDto;
import net.provera.securityserv.dto.UserRegisterDto;
import net.provera.securityserv.dto.UserResetPasswordDto;
import net.provera.securityserv.util.TPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {


    UserDto findOne(String username);

    UserDto save(UserDto userDto);

    UserDto getById(Long id);

    TPage<UserDto> getAllPageable(Pageable pageable);

    List<UserDto> getUsers();

    Boolean delete(Long id);

    UserDto update(Long id, UserDto userDto);

    Boolean register(UserRegisterDto userRegisterDto);

    //Boolean forgotPassword(UserForgotPasswordDto userForgotPasswordDto);

    Boolean resetPassword(UserResetPasswordDto userResetPasswordDto);


    UserDto addRoleByRoleId(Long id, Long roleId);

    UserDto addRoleByRoleName(Long id, String roleName);

}
