package org.saptah.main.practise_demo.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/security_demo")
public class SpringSecurityController {
    private List<User> users = new ArrayList<>(List.of(
            new User(1,"rushi",100),
            new User(2, "rudra",100)
    ));

    @GetMapping("/students")
    public List<User> getUsers(){
        return users;
    }

    @PostMapping("/students")
    public User createStudent(@RequestBody User user){
        users.add(user);
        return user;
    }

    @GetMapping("/csrf_token")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @GetMapping("/")
    public String getHomePage(HttpServletRequest request){
        return "Ram Krishna Hari\n"+request.getSession().getId();
    }

    @GetMapping("/logout_pre_check")
    @ResponseBody
    public String logoutPreCheck(){
        return "logout pre check called.";
    }

    @GetMapping("/logout_success")
    @ResponseBody
    public String logoutSuccessMessage(){
        return "logout successful.";
    }
}

