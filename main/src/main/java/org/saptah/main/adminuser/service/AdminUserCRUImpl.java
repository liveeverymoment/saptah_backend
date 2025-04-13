package org.saptah.main.adminuser.service;

import org.saptah.main.adminuser.dto.AdminUserDTO;
import org.saptah.main.adminuser.dto.AdminUserDTOFromService;
import org.saptah.main.adminuser.entity.AdminUser;
import org.saptah.main.adminuser.repository.AdminUserJPARepository;
import org.saptah.main.adminuser.util.MessagesBetweenLayers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminUserCRUImpl implements AdminUserCRU{

    @Autowired
    private AdminUserJPARepository adminuserjparepository;

    @Autowired
    private AdminUserDTOFromService adminuserdtofromservice;

    @Override
    public AdminUserDTOFromService createAdminUser(AdminUserDTO dto) {
        try {
            if (adminuserjparepository.existsByEmail(dto.getEmail())) {
                adminuserdtofromservice.setData(null);
                adminuserdtofromservice.setMessage("User with this email already exists.");
                adminuserdtofromservice.setOperationSuccess(false);
                return adminuserdtofromservice;
            }
            if (adminuserjparepository.existsByCountryCodeAndMobileNumber(dto.getCountryCode(), dto.getMobileNumber())) {
                adminuserdtofromservice.setData(null);
                adminuserdtofromservice.setMessage("User with this country code + mobile number already exists.");
                adminuserdtofromservice.setOperationSuccess(false);
                return adminuserdtofromservice;
            }
            AdminUser user = adminuserjparepository.save(AdminUser.fromAdminUserDTOToAdminUser(dto));
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
}
