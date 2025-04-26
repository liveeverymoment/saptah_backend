package org.saptah.main.user.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
public class MyHashWrapper {
    private volatile static BCryptPasswordEncoder bcryptpasswordencoder;
    private final static Object lock = new Object();

    private MyHashWrapper() {
        bcryptpasswordencoder = new BCryptPasswordEncoder(6);
    }
    public static BCryptPasswordEncoder getBCryptPasswordEncoderInstance(){
        if(bcryptpasswordencoder==null){
            synchronized (lock){
                    if(bcryptpasswordencoder==null){
                        new MyHashWrapper();
                    }
            }
        }
        return bcryptpasswordencoder;
    }
}
