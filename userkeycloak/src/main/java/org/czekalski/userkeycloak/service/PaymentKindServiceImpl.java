package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.PaymentKindMapper;
import org.czekalski.userkeycloak.repository.PaymentKindRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentKindServiceImpl implements PaymentKindService {

    private final PaymentKindRepository paymentKindRepository;
    private final PaymentKindMapper paymentKindMapper;


    public PaymentKindServiceImpl(PaymentKindRepository paymentKindRepository, PaymentKindMapper paymentKindMapper) {
        this.paymentKindRepository = paymentKindRepository;
        this.paymentKindMapper = paymentKindMapper;
    }


 public  List<PaymentKindCommand> getListOfPaymentKinds(){

        return paymentKindRepository.findAll().stream()
                .map(paymentKindMapper::paymentKindToPaymentKindCommand).collect(Collectors.toList());

 }
}
