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
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.CrossOrigin;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private JSONObject testUserDTOJSON = new JSONObject();
    private JSONObject testLoginJSON = new JSONObject();

    @BeforeEach
    void setUp() throws JSONException {
        testUserDTOJSON.put("username", "username");
        testUserDTOJSON.put("password", "password");
        testUserDTOJSON.put("firstName", "firstName");
        testUserDTOJSON.put("lastName", "lastName");
        testUserDTOJSON.put("phone", "+79998887766");
        testUserDTOJSON.put("role", "ADMIN");

        testLoginJSON.put("password", "password");
        testLoginJSON.put("username", "username");
    }

    @Test
    void testReg() throws Exception {
        mockMvc.perform(
                        post("/register").contentType(MediaType.APPLICATION_JSON).content(testUserDTOJSON.toString()))
                .andExpect(status().isOk());
        mockMvc.perform(
                        post("/login").contentType(MediaType.APPLICATION_JSON).content(testLoginJSON.toString()))
                .andExpect(status().isOk());

    }

    @Test
    void testGetUser() throws Exception {

        mockMvc.perform(
                        post("/login").contentType(MediaType.APPLICATION_JSON).content(testLoginJSON.toString()))
                .andExpect(status().isOk());
        mockMvc.perform(
                        get("/users/me").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value("username"));
//        mockMvc.perform(
//                        post("/pet  s").contentType(MediaType.APPLICATION_JSON).content(testJSONObject.toString()))
//                .andExpect(status().isOk());
//        mockMvc.perform(
//                        get("/pets"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(1))
//                .andExpect(jsonPath("$[0].name").value("testPet"))
//                .andExpect(jsonPath("$[0].id").value("1"))
//                .andExpect(jsonPath("$[0].type").value("test"))
//                .andExpect(jsonPath("$[0].age").value("11"))
//                .andExpect(jsonPath("$[0].description").value("description"));
    }
}
