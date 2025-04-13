package org.saptah.main.adminuser.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AdminUserDTOFromService {
    private Object data;
    private String message;
    private boolean operationSuccess;
}
