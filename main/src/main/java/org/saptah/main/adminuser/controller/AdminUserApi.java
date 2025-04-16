package org.saptah.main.adminuser.controller;

import org.saptah.main.adminuser.dto.AdminUserDTO;
import org.saptah.main.adminuser.dto.AdminUserDTOFromService;
import org.saptah.main.adminuser.dto.ApiResponseDTO;
import org.saptah.main.adminuser.service.AdminUserCRUImpl;
import org.saptah.main.adminuser.util.AdminUserLoginDTOGroup;
import org.saptah.main.adminuser.util.ApiResponseHandler;
import org.saptah.main.adminuser.util.CreateAdminUserDTO;
import org.saptah.main.adminuser.util.MessagesBetweenLayers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/admin/register")
public class AdminUserApi {
    @Autowired
    private AdminUserCRUImpl adminusercruimpl;

    @PostMapping
    public ResponseEntity<?> createAdmin(@Validated(CreateAdminUserDTO.class) @RequestBody AdminUserDTO adminuserdto){
        AdminUserDTOFromService tempDto = adminusercruimpl.createAdminUser(adminuserdto);
        if(tempDto.isOperationSuccess()){
            return ApiResponseHandler.createSuccess(
                    ApiResponseDTO.CreateApiResponseDTO(
                            tempDto.getData(),
                            tempDto.getMessage(),
                            null));
        }else{
            return ApiResponseHandler.operationFailed(
                    ApiResponseDTO.CreateApiResponseDTO(
                            null,
                            MessagesBetweenLayers.AdminUserCreationFailFromController,
                            tempDto.getMessage()), 400
            );
        }
    }

    @GetMapping("/test")
    public String test() {
        return "Ram Krishna Hari!";
    }

}

@RestController
@RequestMapping("/v1/admin/login")
class AdminUserLoginController{

    @Autowired
    private AdminUserCRUImpl adminusercruimpl;

    @PostMapping
    public ResponseEntity<?> loginAdmin(@Validated(AdminUserLoginDTOGroup.class) @RequestBody AdminUserDTO adminUserDTO){
        Map<String, Object> loginOutput = adminusercruimpl.AdminUserLoginValidationAndJWTSend(
                adminUserDTO.getEmail(),adminUserDTO.getPassword());
        if((boolean)loginOutput.get("output")){
            return ApiResponseHandler.operationSuccess(
                    ApiResponseDTO.CreateApiResponseDTO(
                            loginOutput.get("jwt"),
                            (String) loginOutput.get("message"),
                            (String) loginOutput.get("error")));
        }else{
            return ApiResponseHandler.operationFailed(
                    ApiResponseDTO.CreateApiResponseDTO(
                            null,
                            (String) loginOutput.get("message"),
                            (String) loginOutput.get("error")), 400
            );
        }
    }
}
