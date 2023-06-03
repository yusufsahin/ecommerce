package net.provera.securityserv.service.impl;

import net.provera.securityserv.dao.PrivilegeRepository;
import net.provera.securityserv.dao.entity.Privilege;
import net.provera.securityserv.dto.PrivilegeDto;
import net.provera.securityserv.service.PrivilegeService;

import net.provera.securityserv.util.TPage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private ModelMapper modelMapper;



    @Override
    public PrivilegeDto save(PrivilegeDto privilegeDto)  {
        // Optional<Privilege> checkPrivilege= privilegeRepository.findById(privilegeDto.getId());
        //  if(checkPrivilege.isPresent())
        //       throw new  IllegalArgumentException("Privilege Already Exist");
        Privilege privilege = modelMapper.map(privilegeDto, Privilege.class);
        privilege = privilegeRepository.save(privilege);
        privilegeDto.setId(privilege.getId());
        return privilegeDto;

    }

    @Override
    public PrivilegeDto getById(Long id) throws Exception {

        Optional<Privilege> optionalPrivilege = privilegeRepository.findById(id);

        if(!optionalPrivilege.isPresent())
            throw new Exception("Privilege not found");

        return modelMapper.map(optionalPrivilege.get(),PrivilegeDto.class);
    }

    @Override
    public List<PrivilegeDto> getPrivileges() {
        List<Privilege> data= privilegeRepository.findAll();
        return Arrays.asList(modelMapper.map(data,PrivilegeDto[].class));
    }

    @Override
    public TPage<PrivilegeDto> getAllPageable(Pageable pageable) {

        Page<Privilege> data = privilegeRepository.findAll(pageable);
        TPage<PrivilegeDto> response = new TPage<PrivilegeDto>();
        response.setStat(data, Arrays.asList(modelMapper.map(data.getContent(), PrivilegeDto[].class)));
        return response;
    }
    @Override
    public Boolean delete(Long id) {
        privilegeRepository.deleteById(id);
        return true;
    }

    @Override
    public PrivilegeDto update(Long id, PrivilegeDto privilegeDto) {
        Optional<Privilege> optionalPrivilege= privilegeRepository.findById(id);
        if(!optionalPrivilege.isPresent())
            throw  new IllegalArgumentException("Privilege not found");
        optionalPrivilege.get().setName(privilegeDto.getName());
        optionalPrivilege.get().setDescription(privilegeDto.getDescription());
        privilegeRepository.save(optionalPrivilege.get());
        return modelMapper.map(optionalPrivilege, PrivilegeDto.class);

    }

    public Privilege getAsEntityByName(String name){
        try{
            return privilegeRepository.findByName(name);
        }

        catch (Exception e){
            throw new IllegalArgumentException("Privilege not found");
        }

    }

    public Privilege getAsEntityById(Long id){
        try{
            Optional<Privilege> privilege =  privilegeRepository.findById(id);
            if(privilege.isPresent())
                return privilege.get();
            throw new IllegalArgumentException("Privilege not found");
        }

        catch (Exception e){
            throw new IllegalArgumentException("Privilege not found");
        }

    }

}

