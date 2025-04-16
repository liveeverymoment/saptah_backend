package org.saptah.main.adminuser.service;

import org.saptah.main.adminuser.dto.AdminUserDTO;
import org.saptah.main.adminuser.dto.AdminUserDTOFromService;
import org.saptah.main.adminuser.entity.AdminUser;
import org.saptah.main.adminuser.events.OnUserLoginNoVerification;
import org.saptah.main.adminuser.repository.AdminUserJPARepository;
import org.saptah.main.adminuser.util.MessagesBetweenLayers;
import org.saptah.main.adminuser.events.OnUserRegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
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
            AdminUser user = adminuserjparepository.save(AdminUser.fromAdminUserDTOToAdminUser(dto));
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
            AdminUser user = adminuserjparepository.findByEmailAndPassword(email,password);
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
