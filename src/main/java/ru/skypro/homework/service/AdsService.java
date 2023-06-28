package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.dto.FullAds;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.AdsEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.repositories.AdsRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AdsService {

    final private AdsRepository adsRepository;
    final private AdsImageService adsImageService;
    final private UserService userService;

    public AdsService(AdsRepository adsRepository, AdsImageService adsImageService, UserService userService) {
        this.adsRepository = adsRepository;
        this.adsImageService = adsImageService;
        this.userService = userService;
    }

    public ResponseWrapperAds findAllAds() {
        Collection<AdsEntity> adsEntityCollection = adsRepository.findAll();

        Collection<Ads> adsCollection = adsEntityCollectionToAdsCollection(adsEntityCollection);

        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();
        responseWrapperAds.setCount(adsCollection.size());
        responseWrapperAds.setResults(adsCollection);
        return responseWrapperAds;
    }

    public ResponseWrapperAds findAuthorizedUserAds(User author) {
        //раскомментировать когда заработает нормальная авторизация
        //adsEntity.setAuthor(userService.getUserEntity(author.getUsername()));
        UserEntity user = userService.getUserEntity("user@gmail.com");
        Collection<AdsEntity> adsEntityCollection = adsRepository.findAllByAuthor_Id(user.getId());

        Collection<Ads> adsCollection = adsEntityCollectionToAdsCollection(adsEntityCollection);

        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();
        responseWrapperAds.setCount(adsCollection.size());
        responseWrapperAds.setResults(adsCollection);
        return responseWrapperAds;
    }


    public Ads saveNewAd(CreateAds newAds, MultipartFile image, User author) {
        AdsEntity adsEntity = AdsMapper.INSTANCE.createAdsToAdsEntity(newAds);
        //раскомментировать когда заработает нормальная авторизация
        //adsEntity.setAuthor(userService.getUserEntity(author.getUsername()));
        adsEntity.setAuthor(userService.getUserEntity("user@gmail.com"));
        adsEntity.setCreatedAt(LocalDateTime.now());
        adsRepository.save(adsEntity);
        try {
            adsImageService.save(adsEntity, image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Ads ads = AdsMapper.INSTANCE.adsEntityToAds(adsEntity, adsEntity.getAuthor());
        ads.setImage("/ads/image/" + adsImageService.findByAdsId(adsEntity.getId()).getId());
        return ads;
    }

    public FullAds findFullAdsById(int id) {
        AdsEntity adsEntity = adsRepository.findById(id).get();
        FullAds fullAds = AdsMapper.INSTANCE.adsEntityToFullAds(adsEntity, adsEntity.getAuthor());
        if (null!=adsImageService.findByAdsId(adsEntity.getId())) {
            fullAds.setImage("/ads/image/" + adsImageService.findByAdsId(adsEntity.getId()).getId());
        }
        return fullAds;
    }

    public AdsEntity findById(int id) {
        return adsRepository.findById(id).get();
    }


    public void delete(int id) {
        adsImageService.deleteByAdsId(id);
        adsRepository.deleteById(id);
    }

    public Ads updateAds(int id, CreateAds createAds) {
        AdsEntity adsEntity = adsRepository.findById(id).get();
        AdsEntity newAdsEntity = AdsMapper.INSTANCE.createAdsToAdsEntity(createAds);
        adsEntity.setDescription(newAdsEntity.getDescription());
        adsEntity.setTitle(newAdsEntity.getTitle());
        adsEntity.setPrice(newAdsEntity.getPrice());
        return AdsMapper.INSTANCE.adsEntityToAds(adsRepository.save(adsEntity), adsEntity.getAuthor());
    }


    private Collection<Ads> adsEntityCollectionToAdsCollection(Collection<AdsEntity> adsEntityCollection) {
        return adsEntityCollection.stream().map(adsEntity -> {
                    Ads ads = AdsMapper.INSTANCE.adsEntityToAds(adsEntity, adsEntity.getAuthor());
                    if (null== adsImageService.findByAdsId(adsEntity.getId())){
                        return ads;
                    }
                    ads.setImage("/ads/image/" + adsImageService.findByAdsId(adsEntity.getId()).getId());
                    return ads;
                }
        ).collect(Collectors.toList());
    }

}
