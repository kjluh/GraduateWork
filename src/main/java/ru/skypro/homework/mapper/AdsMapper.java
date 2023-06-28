package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.dto.FullAds;
import ru.skypro.homework.entity.AdsEntity;
import ru.skypro.homework.entity.UserEntity;

@Mapper
public interface AdsMapper {
    AdsMapper INSTANCE = Mappers.getMapper(AdsMapper.class);

    @Mapping(source = "adsEntity.id", target = "pk")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.username", target = "email")
    FullAds adsEntityToFullAds(AdsEntity adsEntity, UserEntity author);

    AdsEntity createAdsToAdsEntity(CreateAds createAds);

    @Mapping(source = "adsEntity.id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    Ads adsEntityToAds(AdsEntity adsEntity, UserEntity author);
}
