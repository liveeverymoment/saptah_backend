package org.saptah.main.adminuser.events;

import lombok.Getter;
import org.saptah.main.adminuser.entity.AdminUser;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnUserLoginNoVerification  extends ApplicationEvent {
    private final AdminUser user;
    public OnUserLoginNoVerification(AdminUser user){
        super(user);
        this.user=user;
    }
}
