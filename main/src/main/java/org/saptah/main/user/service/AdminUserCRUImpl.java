package org.saptah.main.user.service;

import lombok.RequiredArgsConstructor;
import org.saptah.main.user.dto.AdminUserDTO;
import org.saptah.main.user.dto.AdminUserDTOFromService;
import org.saptah.main.user.entity.AdminUser;
import org.saptah.main.user.entity.BaseUser;
import org.saptah.main.user.entity.Role;
import org.saptah.main.user.entity.RoleType;
import org.saptah.main.user.events.OnUserLoginNoVerification;
import org.saptah.main.user.repository.BaseUserJPARepository;
import org.saptah.main.user.repository.RoleJPARepository;
import org.saptah.main.user.util.MessagesBetweenLayers;
import org.saptah.main.user.events.OnUserRegistrationCompleteEvent;
import org.saptah.main.user.util.MyHashWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminUserCRUImpl implements AdminUserCRU{

    private final RoleJPARepository rolejparepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private AuthenticationManager authenticationmanager;

    @Autowired
    private BaseUserJPARepository adminuserjparepository;

    @Autowired
    private AdminUserDTOFromService adminuserdtofromservice;

    @Autowired
    private JWTService jwtservice;

    @Override
    public AdminUserDTOFromService createAdminUser(AdminUserDTO dto) {
        try {
            if (adminuserjparepository.existsByEmail(dto.getEmail())) {
                adminuserdtofromservice.setData(null);
                adminuserdtofromservice.setMessage("User with this email already exists.");
                adminuserdtofromservice.setOperationSuccess(false);
                return adminuserdtofromservice;
            }
            Set<Role> roles = new HashSet<>(
                    rolejparepository.findAllByTypeIn(
                            Arrays.asList(RoleType.ROLE_ADMIN,RoleType.ROLE_MANDIR_MODERATOR)
                    ));
            AdminUser fromDTO = AdminUser.fromAdminUserDTOToAdminUser(dto, roles);
            BCryptPasswordEncoder encoder = MyHashWrapper.getBCryptPasswordEncoderInstance();
            String encodedPass = encoder.encode(fromDTO.getPassword());
            // System.out.println("password: "+fromDTO.getPassword()+", encodedPass: "+encodedPass);
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
            Optional<BaseUser> userCheck = adminuserjparepository.findByEmail(email);
            if(userCheck.isEmpty()){
                System.out.println("user not found");
                throw new Exception("user not found");
            }
            BaseUser user = userCheck.get();
            Authentication authentication = authenticationmanager.
                    authenticate(new UsernamePasswordAuthenticationToken(email,password));
            if(authentication.isAuthenticated()){
                if(user.getIsValidated()){
                    // call jwt service to create jwt.
                    String jwt = jwtservice.generateJWT(user);
                    output.put("message","Login successful.");
                    output.put("error",null);
                    output.put("output",true);
                    output.put("jwt",jwt);
                }else{
                    // offload email verification link process
                    applicationEventPublisher.publishEvent(new OnUserLoginNoVerification(user));
                    output.put("message","User email not validated.");
                    output.put("email_validated",false);
                    output.put("error",null);
                    output.put("output",false);
                }
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
