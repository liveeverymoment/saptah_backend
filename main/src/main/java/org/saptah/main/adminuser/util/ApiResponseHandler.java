package org.saptah.main.adminuser.util;

import org.saptah.main.adminuser.dto.ApiResponseDTO;
import org.springframework.http.ResponseEntity;

public class ApiResponseHandler {
    public static ResponseEntity<ApiResponseDTO> createSuccess(ApiResponseDTO dto){
        return ResponseEntity.status(201).body(dto);
    }
    public static ResponseEntity<ApiResponseDTO> createFailed(ApiResponseDTO dto, int status){
        return ResponseEntity.status(status).body(dto);
    }
}
