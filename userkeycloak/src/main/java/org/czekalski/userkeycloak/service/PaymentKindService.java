package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;

import java.util.List;

public interface PaymentKindService {

    List<PaymentKindCommand> getListOfPaymentKinds();
}
