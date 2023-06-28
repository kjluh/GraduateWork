package ru.skypro.homework.controller;

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
                                         @RequestPart MultipartFile image,
                                         @AuthenticationPrincipal User authUser) {

        return ResponseEntity.ok(adsService.saveNewAd(properties, image, authUser));
    }

    @GetMapping("{id}")
    public ResponseEntity<FullAds> findAdsById(@PathVariable int id) {
        return ResponseEntity.ok(adsService.findFullAdsById(id));
    }

    @DeleteMapping("{id}")
    public void deleteAds(@PathVariable int id) {
        adsService.delete(id);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Ads> updateAds(@PathVariable int id, @RequestBody CreateAds createAds) {
        return ResponseEntity.ok(adsService.updateAds(id, createAds));
    }

    @GetMapping("me")
    public ResponseEntity<ResponseWrapperAds> getAuthorizedUserAds(@AuthenticationPrincipal User authUser) {
        return ResponseEntity.ok(adsService.findAuthorizedUserAds(authUser));
    }

    @PatchMapping(value = "/{id}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArrayList<String>> updateAvatar(@PathVariable int id, @RequestPart MultipartFile image) {
        // Обновление аватарки
        return ResponseEntity.ok(adsImageService.updateCover(id, image));
    }


    // Эндпоит получения картинки с сервера
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
