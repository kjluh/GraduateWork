package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/set_password")
    public ResponseEntity updatePassword(@RequestBody NewPassword newPass, Authentication authentication) {
        userService.updatePassword(newPass, authentication.getName());
       return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUser(Authentication authentication) {

        return ResponseEntity.ok(userService.getUser(authentication.getName()));
    }

    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody User user, Authentication authentication) {
        return ResponseEntity.ok(userService.updateUser(user, authentication.getName()));
    }

    @PatchMapping(value = "/me/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateAvatar(@RequestParam MultipartFile image, Authentication authentication) throws IOException {
        userService.updateAvatar(image, authentication.getName());
        return ResponseEntity.ok().build();
    }




}
