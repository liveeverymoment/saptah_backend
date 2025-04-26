package org.saptah.main.user.util;

import org.saptah.main.user.entity.AdminUser;
import org.saptah.main.user.entity.VerificationToken;

import java.time.LocalDateTime;
import java.util.UUID;

public class Utility {
    public static VerificationToken generateTokenForGivenAdminUser(AdminUser user){
        VerificationToken token = new VerificationToken();
        String tokenString = UUID.randomUUID().toString();
        token.setBaseUser(user);
        token.setToken(tokenString);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusHours(24));
        token.setUsed(false);
        return token;
    }
}
