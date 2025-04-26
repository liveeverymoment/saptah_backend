package org.saptah.main.user.events;

import lombok.Getter;
import org.saptah.main.user.entity.AdminUser;
import org.saptah.main.user.entity.BaseUser;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnUserRegistrationCompleteEvent extends ApplicationEvent {
    private final BaseUser user;
    public OnUserRegistrationCompleteEvent(BaseUser user){
        super(user);
        this.user = user;
    }
}
