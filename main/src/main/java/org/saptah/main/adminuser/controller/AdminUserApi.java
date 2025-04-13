package org.saptah.main.adminuser.controller;

import org.saptah.main.adminuser.dto.AdminUserDTO;
import org.saptah.main.adminuser.dto.AdminUserDTOFromService;
import org.saptah.main.adminuser.dto.ApiResponseDTO;
import org.saptah.main.adminuser.service.AdminUserCRUImpl;
import org.saptah.main.adminuser.util.ApiResponseHandler;
import org.saptah.main.adminuser.util.CreateAdminUserDTO;
import org.saptah.main.adminuser.util.MessagesBetweenLayers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin")
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
            return ApiResponseHandler.createFailed(
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
