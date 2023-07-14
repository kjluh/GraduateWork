package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AdsImage;

public interface AdsImageRepository extends JpaRepository<AdsImage, Integer> {
    AdsImage findAdsImagesByAds_Id(int adsId);
    void deleteAdsImagesByAds_Id(int adsId);
}
