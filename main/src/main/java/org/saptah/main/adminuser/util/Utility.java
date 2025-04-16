package org.saptah.main.adminuser.util;

import org.saptah.main.adminuser.entity.AdminUser;
import org.saptah.main.adminuser.entity.VerificationToken;

import java.time.LocalDateTime;
import java.util.UUID;

public class Utility {
    public static VerificationToken generateTokenForGivenAdminUser(AdminUser user){
        VerificationToken token = new VerificationToken();
        String tokenString = UUID.randomUUID().toString();
        token.setAdminUser(user);
        token.setToken(tokenString);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusHours(24));
        token.setUsed(false);
        return token;
    }
}
