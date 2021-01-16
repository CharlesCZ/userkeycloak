package org.czekalski.userkeycloak.repository;

import org.czekalski.userkeycloak.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource("classpath:application-development.properties")
class StatusRepositoryIT {

   private static final String PREPARED = "Prepared";
    private static final String STARTED = "Started";
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StatusRepository statusRepository;


    @Test
    void findByName() {
        //given
        Status status1=new Status();
        status1.setName(PREPARED);
        entityManager.persist(status1);
        Status status2=new Status();
        status2.setName(STARTED);
        entityManager.persist(status2);
        entityManager.flush();

        //when
        Optional<Status> foundStatus=statusRepository.findByName(PREPARED);

        //then
        assertTrue(foundStatus.isPresent());
        assertEquals(PREPARED,foundStatus.get().getName());

    }
}