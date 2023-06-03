package net.provera.securityserv.service.impl;

import net.provera.securityserv.dao.RoleRepository;
import net.provera.securityserv.dao.entity.Privilege;
import net.provera.securityserv.dao.entity.Role;
import net.provera.securityserv.dto.RoleDto;
import net.provera.securityserv.service.PrivilegeService;
import net.provera.securityserv.service.RoleService;
import net.provera.securityserv.util.TPage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PrivilegeService privilegeService;



    @Override
    public RoleDto save(RoleDto roleDto)  {
        // Optional<Role> checkRole= roleRepository.findById(roleDto.getId());
        //  if(checkRole.isPresent())
        //       throw new  IllegalArgumentException("Role Already Exist");
        Role role = modelMapper.map(roleDto, Role.class);
        role = roleRepository.save(role);
        roleDto.setId(role.getId());
        return roleDto;

    }

    @Override
    public RoleDto getById(Long id) throws Exception {

        Optional<Role> optionalRole = roleRepository.findById(id);

        if(!optionalRole.isPresent())
            throw new Exception("Role not found");

        return modelMapper.map(optionalRole.get(),RoleDto.class);
    }


    @Override
    public List<RoleDto> getRoles() {
        List<Role> data= roleRepository.findAll();
        return Arrays.asList(modelMapper.map(data,RoleDto[].class));
    }

    @Override
    public TPage<RoleDto> getAllPageable(Pageable pageable) {

        Page<Role> data = roleRepository.findAll(pageable);
        TPage<RoleDto> response = new TPage<RoleDto>();
        response.setStat(data, Arrays.asList(modelMapper.map(data.getContent(), RoleDto[].class)));
        return response;
    }

    @Override
    public Boolean delete(Long id) {
        roleRepository.deleteById(id);
        return true;
    }

    @Override
    public RoleDto update(Long id, RoleDto roleDto) {
        Optional<Role> optionalRole= roleRepository.findById(id);
        if(!optionalRole.isPresent())
            throw  new IllegalArgumentException("Role not found");
        optionalRole.get().setName(roleDto.getName());
        optionalRole.get().setDescription(roleDto.getDescription());
        roleRepository.save(optionalRole.get());
        return modelMapper.map(optionalRole, RoleDto.class);
    }

    public RoleDto getByName(String name){
        Role role = roleRepository.findByName(name);
        if(role == null)
            throw new IllegalArgumentException("Role not found");
        return modelMapper.map(role,RoleDto.class);
    }




    @Override
    public RoleDto addPrivilegeByPrivilegeId(Long id, Long privilegeId){
        try{
            Optional<Role> role = roleRepository.findById(id);
            if(role.isPresent()){
                List<Privilege> oldPrivileges = new ArrayList<>();
                Collection<Privilege> privileges = role.get().getPrivileges();
                boolean hasPrivilege = false;
                for(Privilege p : privileges){
                    boolean isCopied = false;
                    if(p.getId() == privilegeId){
                        hasPrivilege = true;
                    }
                    for(Privilege p2 : oldPrivileges){
                        if(p.getName() == p2.getName()){
                            isCopied = true;
                        }
                    }
                    if(!isCopied)
                        oldPrivileges.add(p);
                }
                if(!hasPrivilege){
                    Privilege privilege =privilegeService.getAsEntityById(privilegeId);
                    oldPrivileges.add(privilege);
                    role.get().setPrivileges(oldPrivileges);
                    roleRepository.save(role.get());
                    return modelMapper.map(role.get(),RoleDto.class);
                }

                throw new IllegalArgumentException("User already has this role");
            }
            throw new IllegalArgumentException("User Not Found");
        }
        catch (Exception e){
            throw new IllegalArgumentException("Role or User Not Found");
        }
    }

    @Override
    public RoleDto addPrivilegeByPrivilegeName(Long id, String privilegeName){
        try{
            Optional<Role> role = roleRepository.findById(id);
            if(role.isPresent()){
                List<Privilege> oldPrivileges = new ArrayList<>();
                Collection<Privilege> privileges = role.get().getPrivileges();
                boolean hasPrivilege = false;
                for(Privilege p : privileges){
                    boolean isCopied = false;
                    if(p.getName() == privilegeName){
                        hasPrivilege = true;
                    }
                    for(Privilege p2 : oldPrivileges){
                        if(p.getName() == p2.getName()){
                            isCopied = true;
                        }
                    }
                    if(!isCopied)
                        oldPrivileges.add(p);
                }
                if(!hasPrivilege){
                    Privilege privilege = privilegeService.getAsEntityByName(privilegeName);
                    oldPrivileges.add(privilege);
                    role.get().setPrivileges(oldPrivileges);
                    roleRepository.save(role.get());
                    return modelMapper.map(role.get(),RoleDto.class);
                }

                throw new IllegalArgumentException("User already has this role");
            }
            throw new IllegalArgumentException("User Not Found");
        }
        catch (Exception e){
            throw new IllegalArgumentException("Role or User Not Found");
        }
    }

    public Role getAsEntityByName(String name){
        try{
            return roleRepository.findByName(name);
        }

        catch (Exception e){
            throw new IllegalArgumentException("Role not found");
        }

    }

    public Role getAsEntityById(Long id){
        try{
            Optional<Role> role =  roleRepository.findById(id);
            if(role.isPresent())
                return role.get();
            throw new IllegalArgumentException("role not found");
        }

        catch (Exception e){
            throw new IllegalArgumentException("Role not found");
        }

    }

}
