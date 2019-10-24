package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.PaymentKindMapper;
import org.czekalski.userkeycloak.model.PaymentKind;
import org.czekalski.userkeycloak.repository.PaymentKindRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PaymentKindServiceTest {

    @Mock
    PaymentKindRepository paymentKindRepository;

    PaymentKindMapper paymentKindMapper=PaymentKindMapper.INSTANCE;

    PaymentKindService paymentKindService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        paymentKindService=new PaymentKindServiceImpl(paymentKindRepository,paymentKindMapper);
    }

    @Test
    void getListOfPaymentKinds() {
        PaymentKind pk1=new PaymentKind();
        pk1.setId(1L);
        PaymentKind pk2=new PaymentKind();
        pk2.setId(2L);
        given(paymentKindRepository.findAll()).willReturn(Arrays.asList(pk1,pk2));

        List<PaymentKindCommand> paymentKindCommandList=paymentKindService.getListOfPaymentKinds();

        assertThat(paymentKindCommandList).hasSize(2);
        assertEquals(Long.valueOf(2L),paymentKindCommandList.get(1).getId());

    }
}