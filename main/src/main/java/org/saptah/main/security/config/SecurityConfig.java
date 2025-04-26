package org.saptah.main.security.config;

import lombok.RequiredArgsConstructor;
import org.saptah.main.security.MyJWTFilter;
import org.saptah.main.security.MyUserDetailService;
import org.saptah.main.user.util.MyHashWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final String[] allowedUrls = new String[]{
           "/v1/admin/login",
           "/v1/admin/register",
           "/v1/test",
           "/v1/activate/**"
    };

    private final MyUserDetailService myuserdetailservice;

    private final MyJWTFilter myjwtfilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(Customizer.withDefaults());
        http.sessionManagement(session->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        http.authorizeHttpRequests(requests->
                requests.requestMatchers(allowedUrls).permitAll()
                        .anyRequest().authenticated());
        http.addFilterBefore(myjwtfilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myuserdetailservice);
        BCryptPasswordEncoder encoder = MyHashWrapper.getBCryptPasswordEncoderInstance();
        provider.setPasswordEncoder(encoder);
        return provider;
    }
}
