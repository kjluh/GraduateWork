package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AvatarUserEntity;

public interface AvatarUserEntityRepository extends JpaRepository<AvatarUserEntity,Integer> {
}
