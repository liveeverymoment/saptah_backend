package org.saptah.main.adminuser.util;

import org.saptah.main.adminuser.entity.AdminUser;
import org.saptah.main.adminuser.entity.VerificationToken;
import org.saptah.main.adminuser.events.OnUserLoginNoVerification;
import org.saptah.main.adminuser.events.OnUserRegistrationCompleteEvent;
import org.saptah.main.adminuser.repository.AdminUserJPARepository;
import org.saptah.main.adminuser.service.EmailService;
import org.saptah.main.adminuser.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RegistrationListener {

    @Autowired
    private AdminUserJPARepository adminuserjparepository;

    @Autowired
    private EmailService emailservice;

    @Autowired
    private TokenService tokenservice;

    public void generateTokenSendEmail(AdminUser user){
        Map<String,Object> tokenGenerateOutput = tokenservice.userRegistrationDoneCreateToken(user);
        if(tokenGenerateOutput.get("token")==null){
            System.out.println("User can not be validated, ask user to login.");
        }
        boolean emailOutput = emailservice.sendVerificationLinkEmailToAdminUser(
                tokenGenerateOutput.get("token").toString(), user.getEmail());
        if(!emailOutput){
            System.out.println("User verification link is not sent, ask user to login.");
        }
        System.out.println("AdminUser verification email sent successfully.");
    }

    @Async
    @EventListener
    public void handleRegistrationComplete(OnUserRegistrationCompleteEvent event){
        AdminUser user = event.getUser();
        generateTokenSendEmail(user);
    }

    @Async
    @EventListener
    public void handleLoginSuccessNoValidation(OnUserLoginNoVerification event){
        /*
        Approach:
        Get all verification tokens for user
        Check latest one with id for expiry time
        If it's expiry is more than now, that means link is correct
            DO NOTHING
        If it's expiry is less than now, that means
            WE NEED TO SEND NEW LINK
         */
        long id = event.getUser().getId();
        Optional<AdminUser> optionalUser = adminuserjparepository.findByIdWithTokens(id);
        if(optionalUser.isEmpty()){
            System.out.println("Invalid condition");
            return;
        }
        AdminUser user = optionalUser.get();
        List<VerificationToken> tokenList = user.getVerificationTokens();
        Optional<VerificationToken> token = tokenList.stream()
                .filter(verificationToken -> !verificationToken.isUsed())
                .max(Comparator.comparing(VerificationToken::getExpiresAt));
        token.ifPresentOrElse(verificationToken -> {
            if(LocalDateTime.now().isAfter(verificationToken.getExpiresAt())){
                generateTokenSendEmail(user);
            }
        }, ()->generateTokenSendEmail(user));
    }
}
