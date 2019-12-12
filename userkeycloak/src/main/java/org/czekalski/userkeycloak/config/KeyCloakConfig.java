package org.czekalski.userkeycloak.config;



import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.authentication.KeycloakLogoutHandler;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;

import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@ComponentScan(
        basePackageClasses = KeycloakSecurityComponents.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.keycloak.adapters.springsecurity.management.HttpSessionManager"))
@EnableWebSecurity
@ConditionalOnProperty(value = "keycloak.enabled", matchIfMissing = true)
public class KeyCloakConfig extends KeycloakWebSecurityConfigurerAdapter {


@Autowired
KeycloakConfigResolver keycloakConfigResolver;

    @Autowired
    public KeycloakClientRequestFactory keycloakClientRequestFactory;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public KeycloakRestTemplate keycloakRestTemplate() {
        return new KeycloakRestTemplate(keycloakClientRequestFactory);
    }

        /**
         * Registers the KeycloakAuthenticationProvider with the authentication manager.
         */
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();

            // adding proper authority mapper for prefixing role with "ROLE_"
            keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());

            auth.authenticationProvider(keycloakAuthenticationProvider);
        }

        /**
         * Provide a session authentication strategy bean which should be of type
         * RegisterSessionAuthenticationStrategy for public or confidential applications
         * and NullAuthenticatedSessionStrategy for bearer-only applications.
         */
        @Bean
        @Override
        protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
            return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
        }

        /**
         * Use properties in application.properties instead of keycloak.json
         */
        @Bean
        public KeycloakConfigResolver KeycloakConfigResolver() {
            return new KeycloakSpringBootConfigResolver();
        }

        /**
         * Secure appropriate endpoints
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            http.headers().frameOptions().disable();
            http
                  //  .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/test2*").hasRole("user") // only user with role user are allowed to access
                    .antMatchers("/dish/**","/ingredients/**","/orders/allOrders","/orders/{id}/details","/orders/{id}/details/**","/dashboard/**","/dishesToEdit*").hasRole("admin")
                    .antMatchers("/logged*","/logout*","/dashboard*").authenticated()
                    .anyRequest().permitAll()
                    .and()
                        .logout()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/logout/index")
                        .invalidateHttpSession(true) //true by default
                    .addLogoutHandler(keycloakLogoutHandler());
                     //   .addLogoutHandler(new KeycloakLogoutHandler(new AdapterDeploymentContext(keycloakConfigResolver)));


        }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web
                .ignoring()
                .antMatchers("/webjars/**","/static/**");
    }
}
