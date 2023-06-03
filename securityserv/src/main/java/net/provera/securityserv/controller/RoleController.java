package net.provera.securityserv.controller;



import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.provera.securityserv.dto.RoleDto;
import net.provera.securityserv.service.impl.RoleServiceImpl;
import net.provera.securityserv.util.ApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiPaths.RoleCtrl.CTRL)
@Slf4j
// @SecurityRequirement(name = "Bearer Authentication")
// @Tag(name = ApiPaths.RoleCtrl.CTRL, description = "Role API")
public class RoleController {

    @Autowired
    private RoleServiceImpl roleServiceImpl;


    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @GetMapping()
   // @Operation(summary = "Get All Operation")
   // @ApiResponses(value = {
   //         @ApiResponse(responseCode = "200", description = "Successful Operation",
   //                 content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoleDto.class)))
   //         )})
    public ResponseEntity<List<RoleDto>> getAll() {

        List<RoleDto> data = roleServiceImpl.getRoles();
        return ResponseEntity.ok(data);
    }

    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @GetMapping("/{id}")
 //  @Operation(summary = "Get By Id Operation")
 //  @ApiResponses(value = {
 //          @ApiResponse(responseCode = "200",description = "Succesful Operation",content = @Content(schema = @Schema(implementation = RoleDto.class))),
 //          @ApiResponse(responseCode = "404", description = "Role not found")
 //  })
    public ResponseEntity<RoleDto> getById(@PathVariable(value = "id", required = true) Long id) {

        try {

            RoleDto roleDto = roleServiceImpl.getById(id);
            return ResponseEntity.ok(roleDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //return 404, with null body
        }


    }

    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @PostMapping
   // @Operation(summary = "Create Operation")
   // @ApiResponses(value = {
   //         @ApiResponse(responseCode = "201",description = "Succesfull Operation",content = @Content(schema = @Schema(implementation = RoleDto.class))),
   //         @ApiResponse(responseCode = "409", description = "Role could not be created")
   // })
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleDto roleDto) {

        try {

            RoleDto newRole= roleServiceImpl.save(roleDto);

            return ResponseEntity.created(new URI(ApiPaths.RoleCtrl.CTRL+"/"+newRole.getId())).body(newRole);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }


    }

    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @PutMapping("/{id}")
  //  @Operation(summary = "Update Operation")
  //  @ApiResponses(value = {
  //          @ApiResponse(responseCode = "204",description = "Succesfull Operation",content = @Content(schema = @Schema(implementation = RoleDto.class))),
  //          @ApiResponse(responseCode = "404", description = "Role not found ")
  //  })
    public ResponseEntity<RoleDto> updateRole(@PathVariable(value = "id", required = true) Long id, @Valid @RequestBody RoleDto roleDto) {
        try {
            roleServiceImpl.update(id, roleDto);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PreAuthorize("hasAuthority('DELETE_PRIVILEGE')")
    @DeleteMapping("/{id}")
 //   @Operation(summary = "Delete Operation")
 //   @ApiResponses(value = {
 //           @ApiResponse(responseCode = "204",description = "Succesfull Operation",content = @Content(schema = @Schema(implementation = Boolean.class))),
 //           @ApiResponse(responseCode = "400", description = "Id cannot be null"),
 //           @ApiResponse(responseCode = "404", description = "An error occured")
 //   })
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id", required = true) Long id) {
        try {
            if(id!=null)
            {

                Boolean isDeleted = roleServiceImpl.delete(id);
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
    @PutMapping("/{id}/privilege/{privilegeid}")
  //  @Operation(summary = "Add Privilege By PrivilegeId Operation")
  //  @ApiResponses(value = {
  //          @ApiResponse(responseCode = "204",description = "Succesfull Operation",content = @Content(schema = @Schema(implementation = RoleDto.class))),
  //          @ApiResponse(responseCode = "400", description = "Id cannot be null"),
  //          @ApiResponse(responseCode = "404", description = "An error occured")
  //  })
    public ResponseEntity<RoleDto> addRole(@PathVariable(value = "id") Long id, @PathVariable(value = "privilegeid") Long privilegeId){
        try{
            if (id!= null && privilegeId != null){
                roleServiceImpl.addPrivilegeByPrivilegeId(id,privilegeId);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/privilege")
   // @Operation(summary = "Add Privilege By PrivilegeName Operation")
   // @ApiResponses(value = {
   //         @ApiResponse(responseCode = "204",description = "Succesfull Operation",content = @Content(schema = @Schema(implementation = RoleDto.class))),
   //         @ApiResponse(responseCode = "400", description = "Id and PrivilegeName cannot be null"),
   //         @ApiResponse(responseCode = "404", description = "An error occured")
   // })
    public ResponseEntity<RoleDto> addRole(@PathVariable(value = "id") Long id, @RequestParam String privilegeName){
        try{
            if (id!= null && privilegeName != null){
                roleServiceImpl.addPrivilegeByPrivilegeName(id,privilegeName);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }



}

