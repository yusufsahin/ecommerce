package net.provera.securityserv.service;

import net.provera.securityserv.dao.entity.Privilege;
import net.provera.securityserv.dto.PrivilegeDto;
import net.provera.securityserv.util.TPage;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PrivilegeService {

    PrivilegeDto save(PrivilegeDto privilegeDto);

    PrivilegeDto getById(Long id) throws Exception;

    List<PrivilegeDto> getPrivileges();

    TPage<PrivilegeDto> getAllPageable(Pageable pageable);

    Boolean delete(Long id);

    PrivilegeDto update(Long id, PrivilegeDto privilegeDto);

    //Boolean register(UserRegisterDto userRegisterDto);

    //Boolean forgotPassword(UserForgotPasswordDto userForgotPasswordDto);

    //Boolean resetPassword(UserResetPasswordDto userResetPasswordDto);

    Privilege getAsEntityById(Long id);

    Privilege getAsEntityByName(String name);

}
