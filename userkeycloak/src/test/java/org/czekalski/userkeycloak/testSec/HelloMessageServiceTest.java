package org.czekalski.userkeycloak.testSec;

import org.czekalski.userkeycloak.config.KeyCloakConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;


@SpringJUnitConfig(classes = {HelloMessageService.class, KeyCloakConfig.class,MethodSecurityConfig.class})
class HelloMessageServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Autowired
    MessageService messageService;

    @Autowired
    AdapterConfig adapterConfig;

    @Test
    public void getMessageUnauthenticated() {

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class ,() ->{ messageService.getMessage();});
    }
    @WithMockUser
    @Test
    void helloTest() {
        System.out.println(messageService.getMessage());
    }
}