package net.provera.securityserv.service;


import net.provera.securityserv.dao.entity.Role;
import net.provera.securityserv.dto.RoleDto;
import net.provera.securityserv.util.TPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {

    RoleDto save(RoleDto roleDto);

    RoleDto getById(Long id) throws Exception;

    List<RoleDto> getRoles();

    TPage<RoleDto> getAllPageable(Pageable pageable);

    Boolean delete(Long id);

    RoleDto update(Long id, RoleDto roleDto);

    RoleDto getByName(String name);


    RoleDto addPrivilegeByPrivilegeId(Long id, Long privilegeId);

    RoleDto addPrivilegeByPrivilegeName(Long id, String privilegeName);
    //Boolean register(UserRegisterDto userRegisterDto);

    //Boolean forgotPassword(UserForgotPasswordDto userForgotPasswordDto);

    //Boolean resetPassword(UserResetPasswordDto userResetPasswordDto);

    Role getAsEntityByName(String name);

    Role getAsEntityById(Long id);
}
