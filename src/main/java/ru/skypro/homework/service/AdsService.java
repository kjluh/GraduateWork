package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
import ru.skypro.homework.security.MyUserDetails;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AdsService {

    final private AdsRepository adsRepository;
    final private AdsImageService adsImageService;
    @Lazy
    @Autowired
    private CommentService commentService;
    final private UserService userService;
    private final MyUserDetails myUserDetails; // спринг секьюрити сюда положит авторизированного пользователя

    public AdsService(AdsRepository adsRepository, AdsImageService adsImageService, UserService userService, MyUserDetails myUserDetails) {
        this.adsRepository = adsRepository;
        this.adsImageService = adsImageService;
        this.userService = userService;
        this.myUserDetails = myUserDetails;
    }


    /**
     * Показать все объявления
     * @return ДТО обертка для коллекции объявлений
     */
    public ResponseWrapperAds findAllAds() {
        Collection<AdsEntity> adsEntityCollection = adsRepository.findAll();
        Collection<Ads> adsCollection = adsEntityCollectionToAdsCollection(adsEntityCollection);
        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();
        responseWrapperAds.setCount(adsCollection.size());
        responseWrapperAds.setResults(adsCollection);
        return responseWrapperAds;
    }

    /**
     * Вернуть все объявления авторизованного пользователя
     *
     * @return ДТО обертка для коллекции объявлений
     */
    public ResponseWrapperAds findAuthorizedUserAds() {
        UserEntity user = userService.getUserEntity(myUserDetails.getUsername());// чуть переделал авторизацию
        Collection<AdsEntity> adsEntityCollection = adsRepository.findAllByAuthor_Id(user.getId());

        Collection<Ads> adsCollection = adsEntityCollectionToAdsCollection(adsEntityCollection);

        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();
        responseWrapperAds.setCount(adsCollection.size());
        responseWrapperAds.setResults(adsCollection);
        return responseWrapperAds;
    }

    /**
     * Сохранить новое объявление
     * @param newAds новое объявление
     * @param image картинка объявления
     * @return ДТО объявления
     */
    public Ads saveNewAd(CreateAds newAds, MultipartFile image) {
        AdsEntity adsEntity = AdsMapper.INSTANCE.createAdsToAdsEntity(newAds);
        adsEntity.setAuthor(userService.getUserEntity(myUserDetails.getUsername()));
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

    /**
     * Вернуть полное объявление по id
     * @param id
     * @return ДТО полного объявления
     */
    public FullAds findFullAdsById(int id) {
        AdsEntity adsEntity = adsRepository.findById(id).orElseThrow();
        FullAds fullAds = AdsMapper.INSTANCE.adsEntityToFullAds(adsEntity, adsEntity.getAuthor());
        if (null != adsImageService.findByAdsId(adsEntity.getId())) {
            fullAds.setImage("/ads/image/" + adsImageService.findByAdsId(adsEntity.getId()).getId());
        }
        return fullAds;
    }

    /***
     * Вернуть полное объявление не в ДТО
     * @param id
     * @return внутренняя сущность объявления
     */
    public AdsEntity findById(int id) {
        return adsRepository.findById(id).orElseThrow();
    }

    /**
     * Удалить объявление
     * @param id
     * @return
     */
    public boolean delete(int id) {
//        Таким образом проверять что это владелец обьявления или админ
        if (isChoiceRole(id)) {
            try {
                adsImageService.deleteByAdsId(id);
                commentService.deleteAllCommentsByAdsId(id);
                adsRepository.deleteById(id);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Обновить объявление
     * @param id объявления
     * @param createAds новое объявление
     * @return ДТО объявления
     */
    public Ads updateAds(int id, CreateAds createAds) {
        AdsEntity adsEntity = adsRepository.findById(id).orElseThrow();
        AdsEntity newAdsEntity = AdsMapper.INSTANCE.createAdsToAdsEntity(createAds);
        adsEntity.setDescription(newAdsEntity.getDescription());
        adsEntity.setTitle(newAdsEntity.getTitle());
        adsEntity.setPrice(newAdsEntity.getPrice());
        return AdsMapper.INSTANCE.adsEntityToAds(adsRepository.save(adsEntity), adsEntity.getAuthor());
    }

    /**
     * Служебный метод для маппинга коллекции внутренних сущностей в ДТО
     * @param adsEntityCollection
     * @return
     */
    private Collection<Ads> adsEntityCollectionToAdsCollection(Collection<AdsEntity> adsEntityCollection) {
        return adsEntityCollection.stream().map(adsEntity -> {
                    Ads ads = AdsMapper.INSTANCE.adsEntityToAds(adsEntity, adsEntity.getAuthor());
                    if (null == adsImageService.findByAdsId(adsEntity.getId())) {
                        return ads;
                    }
                    ads.setImage("/ads/image/" + adsImageService.findByAdsId(adsEntity.getId()).getId());
                    return ads;
                }
        ).collect(Collectors.toList());
    }

    /**
     * Проверка принадлежности объявления авторизованному пользователю
     * @param id
     * @return
     */
    public boolean isChoiceRole(int id) {
        return (myUserDetails.getUsername().equals(adsRepository.findById(id).orElseThrow().getAuthor().getUsername())
                || myUserDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()).contains("ROLE_ADMIN"));
    }

}
