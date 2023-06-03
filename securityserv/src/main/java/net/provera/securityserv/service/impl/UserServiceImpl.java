package net.provera.securityserv.service.impl;

import net.provera.securityserv.dao.UserRepository;
import net.provera.securityserv.dao.entity.Privilege;
import net.provera.securityserv.dao.entity.Role;
import net.provera.securityserv.dao.entity.User;
import net.provera.securityserv.dto.UserDto;
import net.provera.securityserv.dto.UserRegisterDto;
import net.provera.securityserv.dto.UserResetPasswordDto;
import net.provera.securityserv.service.RoleService;
import net.provera.securityserv.service.UserService;
import jakarta.transaction.Transactional;
import net.provera.securityserv.util.TPage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Period;
import java.util.*;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired()
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleService roleService;


   /* @Autowired
    private EmailSenderService emailSenderService;
    */

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username.toString());
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        List<String> privileges = getPrivileges(user.getRoles());
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        privileges.forEach(privilege -> {
            //authorities.add(new SimpleGrantedAuthority(role.getName()));
            authorities.add(new SimpleGrantedAuthority(privilege));
        });
        return authorities;
        //return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    @Override
    public TPage<UserDto> getAllPageable(Pageable pageable) {

        Page<User> data = userRepository.findAll(pageable);
        TPage<UserDto> response = new TPage<UserDto>();
        response.setStat(data, Arrays.asList(modelMapper.map(data.getContent(), UserDto[].class)));
        return response;
    }
    @Override
    public UserDto save(UserDto userDto) {
        //User checkUser= userRepository.getOne(userDto.getId());
        //if(checkUser!=null)
        //throw new  IllegalArgumentException("User Code Already Exist");
        User u = modelMapper.map(userDto, User.class);
        u = userRepository.save(u);
        userDto.setId(u.getId());
        return userDto;
    }

    @Override
    public UserDto getById(Long id) {
        User u = userRepository.getOne(id);
        return modelMapper.map(u, UserDto.class);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> data = userRepository.findAll();
        return Arrays.asList(modelMapper.map(data, UserDto[].class));
    }

    @Override
    public Boolean delete(Long id) {
        userRepository.deleteById(id);
        return true;
    }

    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        User userDb = userRepository.getOne(id);
        if (userDb == null)
            throw new IllegalArgumentException("User Does Not Exist ID:" + id);

        //userDb.setUserName(userDto.getUserName());
        userDb.setFirstname(userDto.getFirstname());
        userDb.setLastname(userDto.getLastname());
        userDb.setPhonenum(userDto.getPhonenum());
        userDb.setPicture(userDto.getPicture());
        userDb.setFirstname(userDto.getFirstname());
        userDb.setLastname(userDto.getLastname());
        userRepository.save(userDb);
        return modelMapper.map(userDb, UserDto.class);
    }

    @Override
    public UserDto findOne(String username) {
        User u = userRepository.findByUsername(username);
        return modelMapper.map(u, UserDto.class);
    }

    @Transactional
    public Boolean register(UserRegisterDto userRegisterDto) {
        try {
            User user = new User();
            user.setEmail(userRegisterDto.getEmail());
            user.setFirstname(userRegisterDto.getFirstname());
            user.setLastname(userRegisterDto.getLastname());
            user.setPassword(bcryptEncoder.encode(userRegisterDto.getPassword()));
            user.setUsername(userRegisterDto.getUsername());
            user.setRoles(Arrays.asList(roleService.getAsEntityByName("ROLE_MEMBER")));
            userRepository.save(user);
            return Boolean.TRUE;
        } catch (Exception e) {
            //log.error("REGISTRATION=>", e);
            return Boolean.FALSE;
        }

    }

    /*
    @Override
    public Boolean forgotPassword(UserForgotPasswordDto userForgotPasswordDto) {
        try {
            String regex = "^(.+)@(.+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(userForgotPasswordDto.getUsernameoremail());
            Optional<User> userExists=null;
            if(matcher.matches())
            {
                userExists = Optional.ofNullable(userRepository.findByUsernameOrEmail("",userForgotPasswordDto.getUsernameoremail()));
            }
            else
            {
                userExists = Optional.ofNullable(userRepository.findByUsernameOrEmail(userForgotPasswordDto.getUsernameoremail(),""));
            }

            if(userExists.isPresent())
            {
                User userDb= userExists.get();
                userDb.setForgotPasswordGuid(UUID.randomUUID());

                userDb.setForgotPasswordValidDate(Date.from(Instant.now().plus(Period.ofDays(3))));
                userRepository.save(userDb);

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                HttpServletRequest request = null;

                mailMessage.setTo(userDb.getEmail());
                mailMessage.setSubject("Forgot Password Request for PAMERA");
                String emailBody="Forgot Password Request for PAMERA"+System.getProperty("line.separator");
                emailBody+="Please click following link or copy-paste Internet Browser to change password"+System.getProperty("line.separator");
                emailBody +="http://localhost:4200/resetpassword/" + userDb.getForgotPasswordGuid();

                mailMessage.setText(emailBody);

                try{
                    emailSenderService.sendEmail(mailMessage);
                    return  true;
                }
                catch (Exception e)
                {
                    return  false;
                }

            }
            else
            {
                return  false;
            }
        }
        catch(Exception ex)
        {
            return false;
        }
    }
     */

    @Override
    public Boolean resetPassword(UserResetPasswordDto userResetPasswordDto) {
        try
        {
            Optional<User> userExists= Optional.ofNullable(userRepository.findByForgotPasswordGuid(UUID.fromString(userResetPasswordDto.getForgotPasswordGuid().toString())));

            if(userExists.isPresent())
            {

                User userDb= userExists.get();

                if(Date.from(Instant.now()).after(userDb.getForgotPasswordValidDate()))
                {
                    throw  new IllegalArgumentException();
                }

                if(userResetPasswordDto.getNewPassword().equals(userResetPasswordDto.getConfirmPassword()) && userDb.getForgotPasswordGuid().equals(userResetPasswordDto.getForgotPasswordGuid()))
                    userDb.setPassword(bcryptEncoder.encode(userResetPasswordDto.getNewPassword()));

                userDb.setForgotPasswordValidDate(Date.from(Instant.now().minus(Period.ofDays(1))));

                userRepository.save(userDb);
                return  true;
            }
            throw  new IllegalArgumentException();
        }
        catch (Exception e)
        {
            throw  new IllegalArgumentException();
        }
    }



    @Override
    public UserDto addRoleByRoleId(Long id, Long roleId){
        try{
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent()){
                List<Role> oldRoles = new ArrayList<>();
                Collection<Role> roles = user.get().getRoles();
                boolean hasRole = false;
                for(Role r : roles){
                    boolean isCopied = false;
                    if(r.getId() == roleId){
                        hasRole = true;
                    }
                    for(Role r2 : oldRoles){
                        if(r.getName() == r2.getName()){
                            isCopied = true;
                        }
                    }
                    if(!isCopied)
                        oldRoles.add(r);
                }
                if(!hasRole){
                    Role role =roleService.getAsEntityById(roleId);
                    oldRoles.add(role);
                    user.get().setRoles(oldRoles);
                    userRepository.save(user.get());
                    return modelMapper.map(user.get(),UserDto.class);
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
    public UserDto addRoleByRoleName(Long id, String roleName){
        try{
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent()){
                List<Role> oldRoles = new ArrayList<>();
                Collection<Role> roles = user.get().getRoles();
                boolean hasRole = false;
                for(Role r : roles){
                    boolean isCopied = false;
                    if(r.getName() == roleName){
                        hasRole = true;
                    }
                    for(Role r2 : oldRoles){
                        if(r.getName() == r2.getName()){
                            isCopied = true;
                        }
                    }
                    if(!isCopied)
                        oldRoles.add(r);
                }
                if(!hasRole){
                    Role role = roleService.getAsEntityByName(roleName);
                    oldRoles.add(role);
                    user.get().setRoles(oldRoles);
                    userRepository.save(user.get());
                    return modelMapper.map(user.get(),UserDto.class);
                }

                throw new IllegalArgumentException("User already has this role");
            }
            throw new IllegalArgumentException("User Not Found");
        }
        catch (Exception e){
            throw new IllegalArgumentException("Role or User Not Found");
        }
    }


}
