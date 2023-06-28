package ru.skypro.homework.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AdsEntity;

import java.util.Collection;

public interface AdsRepository extends JpaRepository<AdsEntity, Integer> {
    Collection<AdsEntity> findAllByAuthor_Id(int authorId);
}
