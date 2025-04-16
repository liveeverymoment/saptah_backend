package org.saptah.main.adminuser.entity;

import jakarta.persistence.*;
import lombok.*;
import org.saptah.main.adminuser.dto.AdminUserDTO;

import java.util.List;
import java.util.Optional;

@Entity
@Table(
        name = "admin_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email"}),
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
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name_middle_name", nullable = false)
    private String firstNameMiddleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String state;

    @Column
    private String district;

    @Column
    private String taluka;

    @Column(nullable = false)
    private String city;

    @Column(name = "is_validated")
    private Boolean isValidated;

    @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VerificationToken> verificationTokens;

    public static AdminUser fromAdminUserDTOToAdminUser(AdminUserDTO dto){
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
                .build();
    }
}
