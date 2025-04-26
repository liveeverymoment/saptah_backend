package org.saptah.main.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor // required by jpa
@AllArgsConstructor // for convenience
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleType type;

    public Role(RoleType type){
        this.type=type;
    }

    @Override
    public String getAuthority() {
        return type.name();
    }
}
