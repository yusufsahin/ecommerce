package net.provera.securityserv.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
//@Schema(description = "Privilege Data Transfer Object")
public class PrivilegeDto {

    //@Schema(description = "Privilege ID")
    private Long id;

    @NotNull
    //@Schema(description = "Name of the Privilege",required = true)
    private String name;

    //@Schema(description = "Description of Privilege")
    private String description;

}
