package net.provera.securityserv.controller;



import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.provera.securityserv.dto.UserDto;
import net.provera.securityserv.service.impl.UserServiceImpl;
import net.provera.securityserv.util.ApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(ApiPaths.UserCtrl.CTRL)
@Slf4j
@CrossOrigin
@NoArgsConstructor
@AllArgsConstructor
// @SecurityRequirement(name = "Bearer Authentication")
// @Tag(name = ApiPaths.UserCtrl.CTRL, description = "User API")
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;


    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @GetMapping()
 //  @Operation(summary = "Get All Operation")
 //  @ApiResponses(value = {
 //          @ApiResponse(responseCode = "200", description = "Successful Operation",
 //                  content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
 //          )})
    public ResponseEntity<List<UserDto>> getAll() {

        List<UserDto> data = userServiceImpl.getUsers();
        return ResponseEntity.ok(data);
    }

    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @GetMapping("/{id}")
    //@Operation(summary = "Get By Id Operation")
    //@ApiResponses(value = {
    //        @ApiResponse(responseCode = "200",description = "Succesful Operation",content = @Content(schema = @Schema(implementation = UserDto.class))),
    //        @ApiResponse(responseCode = "404", description = "User not found")
    //})
    public ResponseEntity<UserDto> getById(@PathVariable(value = "id", required = true) Long id) {

        try {

            UserDto userDto = userServiceImpl.getById(id);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //return 404, with null body
        }


    }

    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @PostMapping
  // @Operation(summary = "Create Operation")
  // @ApiResponses(value = {
  //         @ApiResponse(responseCode = "201",description = "Succesfull Operation",content = @Content(schema = @Schema(implementation = UserDto.class))),
  //         @ApiResponse(responseCode = "409", description = "User could not be created")
  // })
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {

        try {
            UserDto newUser= userServiceImpl.save(userDto);

            return ResponseEntity.created(new URI(ApiPaths.UserCtrl.CTRL+"/"+newUser.getId())).body(newUser);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }


    }

    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @PutMapping("/{id}")
  //  @Operation(summary = "Update Operation")
  //  @ApiResponses(value = {
  //          @ApiResponse(responseCode = "204",description = "Succesfull Operation",content = @Content(schema = @Schema(implementation = UserDto.class))),
  //          @ApiResponse(responseCode = "404", description = "User not found ")
  //  })
    public ResponseEntity<UserDto> updateUser(@PathVariable(value = "id", required = true) Long id, @Valid @RequestBody UserDto user) {
        try {
            userServiceImpl.update(id, user);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PreAuthorize("hasAuthority('DELETE_PRIVILEGE')")
    @DeleteMapping("/{id}")
   // @Operation(summary = "Delete Operation")
   // @ApiResponses(value = {
   //         @ApiResponse(responseCode = "204",description = "Succesfull Operation",content = @Content(schema = @Schema(implementation = Boolean.class))),
   //         @ApiResponse(responseCode = "400", description = "Id cannot be null"),
   //         @ApiResponse(responseCode = "404", description = "An error occured")
   // })
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id", required = true) Long id) {
        try {
            if(id!=null)
            {

                Boolean isDeleted = userServiceImpl.delete(id);
                return ResponseEntity.ok(isDeleted);
            }
            else
            {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/role/{roleid}")
  //  @Operation(summary = "Add Role By RoleId Operation")
  //  @ApiResponses(value = {
  //          @ApiResponse(responseCode = "204",description = "Succesfull Operation",content = @Content(schema = @Schema(implementation = UserDto.class))),
  //          @ApiResponse(responseCode = "400", description = "Id cannot be null"),
  //          @ApiResponse(responseCode = "404", description = "An error occured")
  //  })
    public ResponseEntity<UserDto> addRole(@PathVariable(value = "id") Long id, @PathVariable(value = "roleid") Long roleId){
        try{
            if (id!= null && roleId != null){
                userServiceImpl.addRoleByRoleId(id,roleId);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/role")
   //@Operation(summary = "Add Role By RoleName Operation")
   //@ApiResponses(value = {
   //        @ApiResponse(responseCode = "204",description = "Succesfull Operation",content = @Content(schema = @Schema(implementation = UserDto.class))),
   //        @ApiResponse(responseCode = "400", description = "Id and RoleName cannot be null"),
   //        @ApiResponse(responseCode = "404", description = "An error occured")
   //})
    public ResponseEntity<UserDto> addRole(@PathVariable(value = "id") Long id, @RequestParam String roleName){
        try{
            if (id!= null && roleName != null){
                userServiceImpl.addRoleByRoleName(id,roleName);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

}
