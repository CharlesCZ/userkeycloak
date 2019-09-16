package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.Service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UserController.class)
@TestPropertySource("classpath:application-development.properties")
class UserControllerTest {

    @MockBean
    KeycloakRestTemplate keycloakRestTemplate;

    @MockBean
    UserService userService;


    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        reset(userService);
    }

    @Test
    @WithMockUser
    void logout() throws Exception {
        mockMvc.perform(get("/logout/index"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")));
    }

    @Test
    @WithMockUser
    void  loggedIn() throws Exception {
        given(userService.getloggedInUser()).willReturn(new AccessToken());

        mockMvc.perform(get("/logged"))
                    .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")))
                .andExpect(view().name("users/logged"))
                .andExpect(model().attributeExists("token"));

        then(userService).should().getloggedInUser();
    }
}