package org.saptah.main.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(
        name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email"}),
        }
)
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@RequiredArgsConstructor
public abstract class BaseUser implements UserDetails {
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

    @OneToMany(mappedBy = "baseUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VerificationToken> verificationTokens;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_roles",
            joinColumns = @JoinColumn(name="user_id"), // owning side
            inverseJoinColumns = @JoinColumn(name="role_id") // inverse side
    )
    private Set<Role> authorities;

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
        // return UserDetails.super.isEnabled();
        return true;
    }
}
