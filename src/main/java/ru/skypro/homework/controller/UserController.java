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
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/set_password")
    public ResponseEntity updatePassword(@RequestBody NewPassword newPass) {
        if (userService.updatePassword(newPass)){
           return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUser() {
        return ResponseEntity.ok(userService.getUser());
    }

    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateAvatar(@RequestParam MultipartFile image) throws IOException {
        userService.updateAvatar(image);
        return ResponseEntity.ok().build();
    }


    //контроллер для возврата массива байт аватара пользователя
    @GetMapping("/avatar/{id}/db")
    public ResponseEntity<byte[]> getUserAvatar(@PathVariable Integer id) throws IOException {
        return ResponseEntity.ok(userService.getURLAvatar(id));
    }
}
