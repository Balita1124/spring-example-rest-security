package com.balita.springexamplecrud.repository;

import com.balita.springexamplecrud.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
