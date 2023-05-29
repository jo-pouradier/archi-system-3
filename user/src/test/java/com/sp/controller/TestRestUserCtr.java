package com.sp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.model.User;
import com.sp.service.UserService;
import fr.dtos.common.user.UserRegisterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = RestUserCtr.class)
class TestRestUserCtr {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() throws Exception {
        mockUser = new User("tp","tp","t.p@tp");
        mockUser.setUUID(UUID.randomUUID());
    }


    @Test
    public void testGetUserById() throws Exception {
        given(userService.getUser(mockUser.getUUID())).willReturn(mockUser);

        mockMvc.perform(get("/getUser/" + mockUser.getUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        given(userService.getUserByEmail(mockUser.getEmail())).willReturn(mockUser);

        mockMvc.perform(get("/getUserByEmail/" + mockUser.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    final void testAddUser() throws Exception {
        given(userService.createUser((any(UserRegisterDTO.class)))).willReturn(mockUser);
        String userJson = "{\"name\":\"tp\",\"password\":\"tp\",\"email\":\"t.p@tp\"}";
        mockMvc.perform(post("/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson).characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    final void testDepotUser() throws Exception{
        given(userService.credit(any(UUID.class),any(Float.class))).willReturn(mockUser);
        mockMvc.perform(get("/depot")
                        .param("uuid",mockUser.getUUID().toString())
                        .param("amount","5000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    final void testDebitNotOKUser() throws Exception{
        given(userService.debit(any(UUID.class),any(Float.class))).willReturn(true);
        given(userService.getUser(any(UUID.class))).willReturn(mockUser);
        mockMvc.perform(get("/debit")
                        .param("uuid",mockUser.getUUID().toString())
                        .param("amount","6000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk());

    }

    @Test
    final void testDebitOKUser() throws Exception{
        given(userService.debit(any(UUID.class),any(Float.class))).willReturn(true);
        given(userService.getUser(any(UUID.class))).willReturn(mockUser);
        mockMvc.perform(get("/debit")
                        .param("uuid",mockUser.getUUID().toString())
                        .param("amount","4000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

}
