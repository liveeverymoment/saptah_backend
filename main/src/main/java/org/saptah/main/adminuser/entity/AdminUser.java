package org.saptah.main.adminuser.entity;

import jakarta.persistence.*;
import lombok.*;
import org.saptah.main.adminuser.dto.AdminUserDTO;

import java.util.Optional;

@Entity
@Table(
        name = "admin_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email"}),
                @UniqueConstraint(columnNames = {"country_code", "mobile_number"})
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUser {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String password;

    @Column(name = "first_name_middle_name", nullable = false)
    String firstNameMiddleName;

    @Column(name = "last_name")
    String lastName;

    @Column(nullable = false)
    String country;

    @Column(nullable = false)
    String state;

    @Column
    String district;

    @Column
    String taluka;

    @Column(nullable = false)
    String city;

    @Column(name = "mobile_number", nullable = false)
    String mobileNumber;

    @Column(name = "country_code", nullable = false)
    String countryCode;

    @Column(name = "is_validated")
    Boolean isValidated;

    public static AdminUser fromAdminUserDTOToAdminUser(AdminUserDTO dto){
        return AdminUser.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .firstNameMiddleName(dto.getFirstNameMiddleName())
                .country(dto.getCountry())
                .state(dto.getState())
                .city(dto.getCity())
                .mobileNumber(dto.getMobileNumber())
                .countryCode(dto.getCountryCode())
                .lastName(Optional.ofNullable(dto.getLastName())
                        .filter(lastname->!lastname.isEmpty())
                        .orElse(null))
                .district(Optional.ofNullable(dto.getDistrict())
                        .filter(district->!district.isEmpty())
                        .orElse(null))
                .taluka(Optional.ofNullable(dto.getTaluka())
                        .filter(taluka->!taluka.isEmpty())
                        .orElse(null))
                .build();
    }
}
