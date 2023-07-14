package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReq;
import org.springframework.security.provisioning.UserDetailsManager;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repositories.UserEntityRepository;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService manager;

    private final UserEntityRepository userEntityRepository;

    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserDetailsService manager, UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.manager = manager;
        this.userEntityRepository = userEntityRepository;
        this.encoder = passwordEncoder;
    }

    @Override
    public boolean login(String userName, String password) {
        UserDetails userDetails = manager.loadUserByUsername(userName);
        if (userDetails == null) {
            return false;
        }
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(RegisterReq registerReq) {
        if (null != userEntityRepository.findByUsername(registerReq.getUsername())) {
            return false;
        }
        UserEntity userEntity = UserMapper.INSTANCE.toEntity(registerReq);
        userEntity.setPassword(encoder.encode(registerReq.getPassword()));
        userEntityRepository.save(userEntity);
        return true;
    }
}