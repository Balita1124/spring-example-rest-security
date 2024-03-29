package com.balita.springexamplecrud.repository;


import com.balita.springexamplecrud.model.RefreshToken;
import com.balita.springexamplecrud.model.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

    @Override
    Optional<UserDevice> findById(Long id);

    Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken);

    Optional<UserDevice> findByUserId(Long userId);
}
