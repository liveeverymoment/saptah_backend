package org.saptah.main.adminuser.service;

import org.saptah.main.adminuser.dto.AdminUserDTO;
import org.saptah.main.adminuser.dto.AdminUserDTOFromService;
import org.saptah.main.adminuser.entity.AdminUser;

public interface AdminUserCRU {
    AdminUserDTOFromService createAdminUser(AdminUserDTO dto);
}
