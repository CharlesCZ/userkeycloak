package org.czekalski.userkeycloak.config.audit;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareBean")
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorAwareBean() {
        return new AuditorAwareBean();
    }

}



