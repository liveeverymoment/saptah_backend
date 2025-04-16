package org.saptah.main.adminuser.controller;

import org.saptah.main.adminuser.dto.ApiResponseDTO;
import org.saptah.main.adminuser.service.TokenService;
import org.saptah.main.adminuser.util.ApiResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/activate")
public class EmailVerificationController {

    @Autowired
    private TokenService tokenservice;

    @GetMapping("/adminuser/{token}")
    public ResponseEntity<?> validateAdminUserEmail(@PathVariable String token){
        Map<String, Object> tokenServiceOutput = tokenservice.validateToken(token);
        if((boolean)tokenServiceOutput.get("output")){
            return ApiResponseHandler.operationSuccess(
                    ApiResponseDTO.CreateApiResponseDTO(
                            null,
                            (String)tokenServiceOutput.get("message"),
                            null));
        }else{
            return ApiResponseHandler.operationFailed(
                    ApiResponseDTO.CreateApiResponseDTO(
                            null,
                            "email validation failed",
                            (String)tokenServiceOutput.get("message")), 400
            );
        }
    }
}
