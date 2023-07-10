package ru.skypro.homework.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.UserSecurity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repositories.UserEntityRepository;
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    private final UserMapper userMapper;

    private final MyUserDetails myUserDetails;

    public MyUserDetailsService(UserEntityRepository userEntityRepository, UserMapper userMapper, MyUserDetails myUserDetails) {
        this.userEntityRepository = userEntityRepository;
        this.userMapper = userMapper;
        this.myUserDetails = myUserDetails;
    }

    /**
     * Проверяет авторизацию пользователя.
     * @param username логин пользователя
     * @return пользователя авторизованного в системе
     * @throws UsernameNotFoundException если не прошла авторизация
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserSecurity userSecurity = userMapper.toDTOSecurity(userEntityRepository.findByUsername(username));
        if (userSecurity == null) {
            throw new UsernameNotFoundException(username+ " not found");
        }
        myUserDetails.setUserSecurity(userSecurity);
        return myUserDetails;
    }
}
