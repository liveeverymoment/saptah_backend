package org.saptah.main.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.saptah.main.user.dto.AdminUserDTO;
import org.saptah.main.user.repository.RoleJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(
        name = "admin_user"
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AdminUser extends BaseUser {

    public static AdminUser fromAdminUserDTOToAdminUser(AdminUserDTO dto, Set<Role> roles){

        return AdminUser.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .firstNameMiddleName(dto.getFirstNameMiddleName())
                .country(dto.getCountry())
                .state(dto.getState())
                .city(dto.getCity())
                .isValidated(false)
                .lastName(Optional.ofNullable(dto.getLastName())
                        .filter(lastname->!lastname.isEmpty())
                        .orElse(null))
                .district(Optional.ofNullable(dto.getDistrict())
                        .filter(district->!district.isEmpty())
                        .orElse(null))
                .taluka(Optional.ofNullable(dto.getTaluka())
                        .filter(taluka->!taluka.isEmpty())
                        .orElse(null))
                .authorities(roles)
                .build();
    }


}
