package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.UserEntity;

public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUsername(String userName);
}
