package org.saptah.main.adminuser.service;

import org.saptah.main.adminuser.entity.AdminUser;
import org.saptah.main.adminuser.entity.VerificationToken;
import org.saptah.main.adminuser.repository.AdminUserJPARepository;
import org.saptah.main.adminuser.repository.VerificationTokenJPARepository;
import org.saptah.main.adminuser.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TokenService {

    @Autowired
    private AdminUserJPARepository adminuserjparepository;

    @Autowired
    private VerificationTokenJPARepository verificationtokenjparepository;

    public Map<String, Object> userRegistrationDoneCreateToken(AdminUser user){
        VerificationToken token = Utility.generateTokenForGivenAdminUser(user);
        Map<String, Object> output = new HashMap<>();
        try{
            verificationtokenjparepository.save(token);
            output.put("message","Created token successfully.");
            output.put("error",null);
            output.put("token",token.getToken());
        }catch (Exception e){
            output.put("message","Error in creating token");
            output.put("error",e.getMessage());
            output.put("token",null);
        }
        return output;
    }

    public Map<String, Object> validateToken(String token) {
        Map<String, Object> output = new HashMap<>();
        try{
            VerificationToken foundToken = verificationtokenjparepository.findByToken(token);
            if(foundToken.isUsed()){
                output.put("message","used token");
                output.put("output",false);
                return output;
            }
            LocalDateTime expiryTime = foundToken.getExpiresAt();
            if(LocalDateTime.now().isAfter(expiryTime)){
                output.put("message","expired token");
                output.put("output",false);
                return output;
            }
            AdminUser user = foundToken.getAdminUser();
            user.setIsValidated(true);
            adminuserjparepository.save(user);
            foundToken.setUsed(true);
            verificationtokenjparepository.save(foundToken);
            output.put("message","user validated");
            output.put("output",true);
            return output;
        } catch (Exception e) {
            System.out.println("find token error. "+e.getMessage());
            output.put("output",false);
            output.put("message",e.getMessage());
            return output;
        }
    }
}
