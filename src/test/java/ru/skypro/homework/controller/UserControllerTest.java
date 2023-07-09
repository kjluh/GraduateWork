package ru.skypro.homework.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private JSONObject testUserDTOJSON = new JSONObject();
    @Autowired
    private WebApplicationContext webApplicationContext;


    private void setTestUserDTOJSON() throws JSONException {
        testUserDTOJSON.put("username", "username");
        testUserDTOJSON.put("password", "password");
        testUserDTOJSON.put("firstName", "firstName");
        testUserDTOJSON.put("lastName", "lastName");
        testUserDTOJSON.put("phone", "+79998887766");
        testUserDTOJSON.put("role", "ADMIN");
    }

    @Test
    void testReg() throws Exception {
        setTestUserDTOJSON();
        JSONObject testLoginJSON = new JSONObject();
        testLoginJSON.put("password", "password");
        testLoginJSON.put("username", "username");
        mockMvc.perform(
                        post("/register").contentType(MediaType.APPLICATION_JSON).content(testUserDTOJSON.toString()))
                .andExpect(status().isOk());
        mockMvc.perform(
                        post("/login").contentType(MediaType.APPLICATION_JSON).content(testLoginJSON.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void testRegTrow() throws Exception {
        setTestUserDTOJSON();
        mockMvc.perform(
                        post("/register").contentType(MediaType.APPLICATION_JSON).content(testUserDTOJSON.toString()))
                .andExpect(status().is(400));
    }

    @Test
    void testGetUser() throws Exception {
        mockMvc.perform(
                        get("/users/me")
                                .header("Authorization", "Basic " +
                                        Base64.getEncoder().encodeToString(("username" + ":" + "password").getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUser() throws Exception {
        JSONObject testUserUpdateDTOJSON = new JSONObject();
        testUserUpdateDTOJSON.put("firstName", "firstName");
        testUserUpdateDTOJSON.put("lastName", "lastName");
        testUserUpdateDTOJSON.put("phone", "+79998887766");
        mockMvc.perform(
                        patch("/users/me").contentType(MediaType.APPLICATION_JSON).content(testUserUpdateDTOJSON.toString())
                                .header("Authorization", "Basic " +
                                        Base64.getEncoder().encodeToString(("username" + ":" + "password").getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUserPassword() throws Exception {
        JSONObject testNewPasswordJSON = new JSONObject();
        testNewPasswordJSON.put("currentPassword", "password");
        testNewPasswordJSON.put("newPassword", "password111");
        mockMvc.perform(
                        post("/users/set_password").contentType(MediaType.APPLICATION_JSON).content(testNewPasswordJSON.toString())
                                .header("Authorization", "Basic " +
                                        Base64.getEncoder().encodeToString(("username" + ":" + "password").getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isOk());
        mockMvc.perform(
                        get("/users/me")
                                .header("Authorization", "Basic " +
                                        Base64.getEncoder().encodeToString(("username" + ":" + "password111").getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isOk());
    }

    @Test
    void testLoadAvatar() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "hello.png", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());

//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(
                        patch("/users/me/image").contentType(MediaType.IMAGE_PNG).content(Objects.requireNonNull(image.getContentType()))
                                .header("Authorization", "Basic " +
                                        Base64.getEncoder().encodeToString(("username" + ":" + "password").getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isOk());
    }
//    @Test
//    void testGetAvatar() throws Exception {
//        MockMultipartFile image = new MockMultipartFile("image", "hello.png", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
//
//        mockMvc.perform(
//                        get("/users/avatar/1/db").content(image.getBytes()))
//                .andExpect(status().isOk());
//    }
}
