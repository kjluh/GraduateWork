package ru.skypro.homework.controller;

import liquibase.pro.packaged.R;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.service.AdsImageService;
import ru.skypro.homework.service.AdsService;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdsController {

    private final AdsService adsService;
    private final AdsImageService adsImageService;

    public AdsController(AdsService adsService, AdsImageService adsImageService) {
        this.adsService = adsService;
        this.adsImageService = adsImageService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        return ResponseEntity.ok(adsService.findAllAds());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ads> saveNewAd(@RequestPart CreateAds properties,
                                         @RequestPart MultipartFile image) {

        return ResponseEntity.ok(adsService.saveNewAd(properties, image));
    }

    @GetMapping("{id}")
    public ResponseEntity<FullAds> findAdsById(@PathVariable int id) {
        return ResponseEntity.ok(adsService.findFullAdsById(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteAds(@PathVariable int id) {
        if (adsService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<Ads> updateAds(@PathVariable int id, @RequestBody CreateAds createAds) {
        if (adsService.isChoiceRole(id)) {
            return ResponseEntity.ok(adsService.updateAds(id, createAds));
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("me")
    public ResponseEntity<ResponseWrapperAds> getAuthorizedUserAds() {
        return ResponseEntity.ok(adsService.findAuthorizedUserAds());
    }

    @PatchMapping(value = "/{id}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArrayList<String>> updateAvatar(@PathVariable int id, @RequestPart MultipartFile image) {
        // Обновление картинки объявления
        return ResponseEntity.ok(adsImageService.updateCover(id, image));
    }


    /**
     * Эндпоит получения картинки с сервера
     * @param id ID пользователя
     * @return ссылку на картинку
     */
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getAdsImage(@PathVariable Integer id) {
        byte[] image = null;
        try {
            image = adsImageService.getAdsImage(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(image);
    }
}
