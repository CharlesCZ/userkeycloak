package org.czekalski.userkeycloak.config.audit;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


public class AuditorAwareBean implements AuditorAware<String> {



    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("default auditor");
        }

        return Optional.of(((AccessToken)((RefreshableKeycloakSecurityContext)((KeycloakPrincipal)((KeycloakAuthenticationToken)authentication).getPrincipal()).getKeycloakSecurityContext()).getToken()).getName());
    }


}


