package org.saptah.main.user.service;

import org.saptah.main.user.dto.AdminUserDTO;
import org.saptah.main.user.dto.AdminUserDTOFromService;

public interface AdminUserCRU {
    AdminUserDTOFromService createAdminUser(AdminUserDTO dto);
}
