package org.saptah.main.practise_demo.security.config;

import org.saptah.main.user.util.MyHashWrapper;
import org.saptah.main.practise_demo.security.MyJWTFilter;
import org.saptah.main.practise_demo.security.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain customSecurifyFilterChain(HttpSecurity http) throws Exception {
//        return http.build();
//    } // this will apply no filter



//    @Bean
//    public SecurityFilterChain customSecurifyFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request->
//                        request.anyRequest().authenticated());
//        return http.build();
//    }
    /*
        this denies every request, since we are not using username, password sent
        in form login and we have not enabled form login.
     */





//    @Bean
//    public SecurityFilterChain customSecurifyFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request->
//                        request.anyRequest().authenticated())
//                .formLogin(Customizer.withDefaults());
//        return http.build();
//    }
    /*
    This now allows us to display login pages of spring security.
    as soon as /logout url is hit, we are not asked confirmation page, it directly
    logs us out.
    Why no logout page: because we have disabled csrf token, if we
    remove that disable operation, then /logout we see logout confirmation page.
     */






//    @Bean
//    public SecurityFilterChain customSecurifyFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request->
//                        request.anyRequest().authenticated());
//        http.formLogin(Customizer.withDefaults());
//        http.logout(logout->
//                logout.logoutUrl("/security_demo/logout_pre_check")
//                        .logoutSuccessUrl("/security_demo/logout_success"));
//        return http.build();
//    }
    /*
    If we hit http://localhost:8081/logout
    we get spring security's default page, and then it shows whitelabel error page
    since no controller is override, and it does not find any controller
    but if we hit /security_demo/logout_pre_check then we are redirected to /login page
    after successful operations: given I only passed @ResponseBody on top of controller methods
    and returned String.
     */







//    @Bean
//    public SecurityFilterChain customSecurifyFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request->
//                        request.anyRequest().authenticated());
//        http.formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults());
//        return http.build();
//    }
    /*
    if we removed http.httpBasic(Customizer.withDefaults()) then postman request
    will show username and password page, even if we sent basic auth.
     */








//    @Bean
//    public SecurityFilterChain customSecurifyFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request->
//                        request.anyRequest().authenticated());
////        http.formLogin(Customizer.withDefaults());
//        http.httpBasic(Customizer.withDefaults());
//        http.sessionManagement(session->
//                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        return http.build();
//    }
    /*
    Now if we want to make our application stateless
    that is we don't want to store jsessionid in cookie, and want to validate each request
    we can add SessionManagement logic above.
    Now what happens:
    for postman: this works fine and for every refresh, we get different session id.
    for browser: this will not work as it will keep redirecting to login url after validation.
        To make it work for browser, we need to comment formLogin().
        Now on browser we see httpBasic given form which will work.
     */






/*
Now I don't want to use uer name and password from application.properties
so I can tell spring security to use my custom user
UserDetailService is interface which is extended by UserDetailManager interface
which is implemented by InMemoryUserDetailsManager class.
Now InMemoryUserDetailsManager has many constructors. One with varargs.
which takes output UserDetails interface which is implemented by
User class of spring security.
 */
//    @Bean
//    public UserDetailsService customUser(){
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("root")
//                .password("root")
//                .roles("USER")
//                .build();
//
//        UserDetails user2 = User
//                .withDefaultPasswordEncoder()
//                .username("rushi")
//                .password("rushi")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user1, user2);
//    }





    @Autowired
    private MyUserDetailService myuserdetailservice;

    @Autowired
    private MyJWTFilter myjwtfilter;
    /*
    AuthenticationProvider is an interface, DaoAuthenticationProvider
    is a class which implements above interface.

    similar to telusko video, I will be creating custom UserDetailService interface
    implementing class.
    When I do that, I have to implemented abstract method called:
    loadUserByUsername
        I don't have username, only email as distinguishing field,
        so can I use same method for that, and do  I get email in the parameter?

            Sending email works.

      Another question:
        Even though I am only checking loadUserByUsername in UserDetailsService
        how wrong password is checked?

            Wrong password is checked because of
            provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());

    here I have to return UserDetails interface implementing class
    So I created my own class implementing UserDetails
     */
    @Bean
    public AuthenticationProvider customAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myuserdetailservice);
        BCryptPasswordEncoder encoder = MyHashWrapper.getBCryptPasswordEncoderInstance();
        provider.setPasswordEncoder(encoder);
        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain customSecurifyFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request->
                        request.requestMatchers("/v1/admin/login").permitAll()
                                .requestMatchers("/v1/admin/register").permitAll()
                                .requestMatchers("/v1/test").permitAll()
                                .requestMatchers("/v1/activate/**").permitAll()
                        .anyRequest().authenticated());
        //http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        http.sessionManagement(session->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(myjwtfilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
