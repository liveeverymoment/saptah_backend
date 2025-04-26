package org.saptah.main.user.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.saptah.main.user.entity.Role;
import org.saptah.main.user.entity.RoleType;
import org.saptah.main.user.repository.RoleJPARepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RoleInitializer {
    private final RoleJPARepository rolejparepository;

    @PostConstruct
    public void init(){
        if(rolejparepository.count()==0){
            Arrays.stream(RoleType.values())
                    .map(Role::new)
                    .forEach(rolejparepository::save);
        }
    }
}
