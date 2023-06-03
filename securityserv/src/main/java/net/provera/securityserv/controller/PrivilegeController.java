package net.provera.securityserv.controller;



import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.provera.securityserv.dto.PrivilegeDto;
import net.provera.securityserv.service.impl.PrivilegeServiceImpl;
import net.provera.securityserv.util.ApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(ApiPaths.PrivilegeCtrl.CTRL)
@CrossOrigin
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
// @SecurityRequirement(name = "Bearer Authentication")
// @Tag(name = ApiPaths.PrivilegeCtrl.CTRL, description = "Privilege API")
public class PrivilegeController {

    @Autowired
    private PrivilegeServiceImpl privilegeServiceImpl;


    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @GetMapping()
   //  @Operation(summary = "Get All Operation")
   //  @ApiResponses(value = {
   //          @ApiResponse(responseCode = "200", description = "Successful Operation",
   //                  content = @Content(array = @ArraySchema(schema = @Schema(implementation = PrivilegeDto.class)))
   //          )})
    public ResponseEntity<List<PrivilegeDto>> getAll() {

        List<PrivilegeDto> data = privilegeServiceImpl.getPrivileges();
        return ResponseEntity.ok(data);
    }

    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @GetMapping("/{id}")
  //// @Operation(summary = "Get By Id Operation")
  //// @ApiResponses(value = {
  ////         @ApiResponse(responseCode = "200",description = "Succesful Operation",content = @Content(schema = @Schema(implementation = PrivilegeDto.class))),
  ////         @ApiResponse(responseCode = "404", description = "Privilege not found")
  //// }
  //// )
    public ResponseEntity<PrivilegeDto> getById(@PathVariable(value = "id", required = true) Long id) {

        try {

            PrivilegeDto privilegeDto = privilegeServiceImpl.getById(id);
            return ResponseEntity.ok(privilegeDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //return 404, with null body
        }


    }

    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @PostMapping
 //  @Operation(summary = "Create Operation")
 //  @ApiResponses(value = {
 //          @ApiResponse(responseCode = "201",description = "Succesfull Operation",content = @Content(schema = @Schema(implementation = PrivilegeDto.class))),
 //          @ApiResponse(responseCode = "409", description = "Privilege could not be created")
 //  })
    public ResponseEntity<PrivilegeDto> createPrivilege(@Valid @RequestBody PrivilegeDto privilegeDto) {

        try {

            PrivilegeDto newPrivilege= privilegeServiceImpl.save(privilegeDto);

            return ResponseEntity.created(new URI(ApiPaths.PrivilegeCtrl.CTRL+"/"+newPrivilege.getId())).body(newPrivilege);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }


    }

    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @PutMapping("/{id}")
  //  @Operation(summary = "Update Operation")
  //  @ApiResponses(value = {
  //          @ApiResponse(responseCode = "204",description = "Succesfull Operation",content = @Content(schema = @Schema(implementation = PrivilegeDto.class))),
  //          @ApiResponse(responseCode = "404", description = "Privilege not found ")
  //  })
    public ResponseEntity<PrivilegeDto> updatePrivilege(@PathVariable(value = "id", required = true) Long id, @Valid @RequestBody PrivilegeDto privilegeDto) {
        try {
            privilegeServiceImpl.update(id, privilegeDto);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PreAuthorize("hasAuthority('DELETE_PRIVILEGE')")
    @DeleteMapping("/{id}")
  //  @Operation(summary = "Delete Operation")
  //  @ApiResponses(value = {
  //          @ApiResponse(responseCode = "204",description = "Succesfull Operation",content = @Content(schema = @Schema(implementation = Boolean.class))),
  //          @ApiResponse(responseCode = "400", description = "Id connot be null"),
  //          @ApiResponse(responseCode = "404", description = "An error occured")
  //  })
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id", required = true) Long id) {
        try {
            if(id!=null)
            {

                Boolean isDeleted = privilegeServiceImpl.delete(id);
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

}
