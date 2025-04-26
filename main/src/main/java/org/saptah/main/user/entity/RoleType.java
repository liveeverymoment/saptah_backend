package org.saptah.main.user.entity;

public enum RoleType{
    ROLE_ADMIN("admin"),
    ROLE_END_USER("end_user"),
    ROLE_MANDIR_MODERATOR("mandir_moderator");

    private String value;
    RoleType(String value){
        this.value=value;
    }
}
