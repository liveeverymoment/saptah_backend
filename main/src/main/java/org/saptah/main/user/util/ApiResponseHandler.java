package org.saptah.main.user.util;

import org.saptah.main.user.dto.ApiResponseDTO;
import org.springframework.http.ResponseEntity;

public class ApiResponseHandler {
    public static ResponseEntity<ApiResponseDTO> createSuccess(ApiResponseDTO dto){
        return ResponseEntity.status(201).body(dto);
    }
    public static ResponseEntity<ApiResponseDTO> operationFailed(ApiResponseDTO dto, int status){
        return ResponseEntity.status(status).body(dto);
    }
    public static ResponseEntity<ApiResponseDTO> operationSuccess(ApiResponseDTO dto){
        return ResponseEntity.status(200).body(dto);
    }
}
