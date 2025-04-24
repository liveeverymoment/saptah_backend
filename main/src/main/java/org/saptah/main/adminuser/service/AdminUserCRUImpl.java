package org.saptah.main.adminuser.service;

import org.saptah.main.adminuser.dto.AdminUserDTO;
import org.saptah.main.adminuser.dto.AdminUserDTOFromService;
import org.saptah.main.adminuser.entity.AdminUser;
import org.saptah.main.adminuser.events.OnUserLoginNoVerification;
import org.saptah.main.adminuser.repository.AdminUserJPARepository;
import org.saptah.main.adminuser.util.MessagesBetweenLayers;
import org.saptah.main.adminuser.events.OnUserRegistrationCompleteEvent;
import org.saptah.main.adminuser.util.MyHashWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminUserCRUImpl implements AdminUserCRU{

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private AdminUserJPARepository adminuserjparepository;

    @Autowired
    private AdminUserDTOFromService adminuserdtofromservice;

    @Override
    public AdminUserDTOFromService createAdminUser(AdminUserDTO dto) {
        try {
            if (adminuserjparepository.existsByEmail(dto.getEmail())) {
                adminuserdtofromservice.setData(null);
                adminuserdtofromservice.setMessage("User with this email already exists.");
                adminuserdtofromservice.setOperationSuccess(false);
                return adminuserdtofromservice;
            }
            AdminUser fromDTO = AdminUser.fromAdminUserDTOToAdminUser(dto);
            BCryptPasswordEncoder encoder = MyHashWrapper.getBCryptPasswordEncoderInstance();
            String encodedPass = encoder.encode(fromDTO.getPassword());
            System.out.println("password: "+fromDTO.getPassword()+", encodedPass: "+encodedPass);
            fromDTO.setPassword(encodedPass);
            AdminUser user = adminuserjparepository.save(fromDTO);
            applicationEventPublisher.publishEvent(new OnUserRegistrationCompleteEvent(user));
            AdminUserDTO dtoData = AdminUserDTO.fromAdminUserToAdminUserDTO(user);
            adminuserdtofromservice.setData(dtoData);
            adminuserdtofromservice.setMessage(MessagesBetweenLayers.AdminUserCreationSuccessFromService);
            adminuserdtofromservice.setOperationSuccess(true);
            return adminuserdtofromservice;
        } catch (Exception e) {
            adminuserdtofromservice.setData(null);
            adminuserdtofromservice.setMessage(e.getMessage());
            adminuserdtofromservice.setOperationSuccess(false);
            return adminuserdtofromservice;
        }
    }

    public Map<String,Object> AdminUserLoginValidationAndJWTSend(String email, String password){
        Map<String, Object> output = new HashMap<>();
        try{
            BCryptPasswordEncoder encoder = MyHashWrapper.getBCryptPasswordEncoderInstance();
            AdminUser user = adminuserjparepository.findByEmail(email);
            if(user==null){
                System.out.println("user not found");
                throw new Exception("user not found");
            }
            boolean match = encoder.matches(password, user.getPassword());
            if(!match){
                System.out.println("invalid user password");
                throw new Exception("invalid user password");
            }
            if(user.getIsValidated()){
                // call jwt service to create jwt.
                output.put("message","Login successful.");
                output.put("error",null);
                output.put("output",true);
                output.put("jwt",null);
            }else{
                // offload email verification link process
                applicationEventPublisher.publishEvent(new OnUserLoginNoVerification(user));
                output.put("message","User email not validated.");
                output.put("email_validated",false);
                output.put("error",null);
                output.put("output",false);
            }
        } catch (Exception e) {
            output.put("message","Incorrect email or password.");
            output.put("email_validated",false);
            output.put("error",e.getMessage());
            output.put("output",false);
        }
        return output;
    }
}
