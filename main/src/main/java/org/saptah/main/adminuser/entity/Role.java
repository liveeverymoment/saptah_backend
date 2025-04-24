package org.saptah.main.adminuser.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {
    ROLE_USER("User"),
    ROLE_ADMIN("Admin");

    private String value;

    Role(String value){
        this.value=value;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
