package org.czekalski.userkeycloak.commadPattern.mapper;

import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;
import org.czekalski.userkeycloak.model.PaymentKind;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentKindMapper {

    PaymentKindMapper INSTANCE= Mappers.getMapper(PaymentKindMapper.class);

    PaymentKindCommand paymentKindToPaymentKindCommand(PaymentKind paymentKind);

    PaymentKind paymentKindCommandToPaymentKind(PaymentKindCommand paymentKindCommand);

}
