package com.balita.springexamplecrud.service;

import com.balita.springexamplecrud.model.Role;
import com.balita.springexamplecrud.repository.RoleRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RoleService {
    private static final Logger logger = Logger.getLogger(RoleService.class);
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    Collection<Role> findAll() {
        return roleRepository.findAll();
    }
}
