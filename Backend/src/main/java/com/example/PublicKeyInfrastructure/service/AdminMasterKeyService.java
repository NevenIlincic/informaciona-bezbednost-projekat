package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.model.AdminMasterKey;
import com.example.PublicKeyInfrastructure.repository.AdminMasterKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminMasterKeyService {

    @Autowired
    private AdminMasterKeyRepository adminMasterKeyRepository;

    public AdminMasterKey getMasterKey() {
        return adminMasterKeyRepository.findById(1).get();
    }

    public AdminMasterKey saveMasterKey(AdminMasterKey masterKey) {
        return adminMasterKeyRepository.save(masterKey);
    }
}
