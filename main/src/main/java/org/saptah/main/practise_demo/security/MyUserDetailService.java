package org.saptah.main.practise_demo.security;

import org.saptah.main.user.entity.BaseUser;
import org.saptah.main.user.repository.BaseUserJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private BaseUserJPARepository baseuserjparepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<BaseUser> userCheck = baseuserjparepository.findByEmail(username);
        if(userCheck.isEmpty()){
            System.out.println("user not found");
            throw new UsernameNotFoundException("user not found");
        }
        return userCheck.get();
    }
}
