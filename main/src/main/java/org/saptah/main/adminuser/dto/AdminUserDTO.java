package org.saptah.main.adminuser.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.saptah.main.adminuser.entity.AdminUser;
import org.saptah.main.adminuser.util.CreateAdminUserDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@Builder
public class AdminUserDTO {
    @NotBlank(message = "Email can not be empty", groups = {CreateAdminUserDTO.class})
    String email;

    @NotBlank(message = "Password can not be empty", groups = {CreateAdminUserDTO.class})
    String password;

    @NotBlank(message = "First name + middle name can not be empty", groups = {CreateAdminUserDTO.class})
    String firstNameMiddleName;

    String lastName;

    @NotBlank(message = "Country can not be empty", groups = {CreateAdminUserDTO.class})
    String country;

    @NotBlank(message = "State can not be empty", groups = {CreateAdminUserDTO.class})
    String state;

    String district;

    String taluka;

    @NotBlank(message = "City can not be empty", groups = {CreateAdminUserDTO.class})
    String city;

    Boolean isValidated;

    public static AdminUserDTO fromAdminUserToAdminUserDTO(AdminUser user){
        return AdminUserDTO.builder()
                .email(user.getEmail())
                .firstNameMiddleName(user.getFirstNameMiddleName())
                .lastName(user.getLastName())
                .country(user.getCountry())
                .state(user.getState())
                .district(user.getDistrict())
                .taluka(user.getTaluka())
                .city(user.getCity())
                .isValidated(user.getIsValidated())
                .build();
    }
}
