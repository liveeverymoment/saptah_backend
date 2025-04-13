package org.saptah.main.adminuser.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseDTO {
    private Object data;
    private String message;
    private String error;
    public static ApiResponseDTO CreateApiResponseDTO(Object data, String message, String error){
        return ApiResponseDTO.builder()
                .data(data)
                .message(message)
                .error(error)
                .build();
    }
}
