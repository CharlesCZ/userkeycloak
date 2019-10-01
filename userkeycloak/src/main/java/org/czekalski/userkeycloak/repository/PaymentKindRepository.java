package org.czekalski.userkeycloak.repository;

import org.czekalski.userkeycloak.model.PaymentKind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentKindRepository extends JpaRepository<PaymentKind,Long> {

    Optional<PaymentKind> findByName(String name);
}
