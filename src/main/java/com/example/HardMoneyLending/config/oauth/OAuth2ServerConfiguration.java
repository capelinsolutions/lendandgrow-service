package com.example.HardMoneyLending.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class OAuth2ServerConfiguration {

    private static final String SERVER_RESOURCE_ID = "oauth2-server";

    private static InMemoryTokenStore tokenStore = new InMemoryTokenStore();

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private UserDetailsService userDetailsService;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.tokenStore(tokenStore).resourceId(SERVER_RESOURCE_ID);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .headers()
                    .frameOptions()
                    .disable()
                    .and()
                    .cors()
                    .and()
                    .rememberMe().userDetailsService(userDetailsService)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/v1/test/").permitAll()
                    .antMatchers("/oauth/**").permitAll()
                    .antMatchers("/api/v1/login/**").permitAll()
                    .antMatchers("/api/v1/file/upload/").authenticated()
                    .antMatchers("/api/v1/file/delete/").authenticated()
                    .antMatchers("/api/v1/property/**").fullyAuthenticated()
                    .antMatchers("/**")
                    .permitAll();
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private PasswordEncoder passwordEncoder;

        /**
         * Setting up the endpointsconfigurer authentication manager.
         * The AuthorizationServerEndpointsConfigurer defines the authorization and token endpoints and the token services.
         * @param endpoints
         * @throws Exception
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .authenticationManager(authenticationManager)
                    .tokenStore(tokenStore);
        }

        /**
         * Setting up the clients with a clientId, a clientSecret, a scope, the grant types and the authorities.
         * @param clients
         * @throws Exception
         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients
                    .inMemory()
                    .withClient("calcerts")
                    .authorizedGrantTypes("client_credentials", "password", "refresh_token")
                    .authorities("ROLE_CLIENT","ROLE_TRUSTED_CLIENT","refresh_token")
                    .scopes("read","write","trust")
                    .resourceIds(SERVER_RESOURCE_ID)
                    .accessTokenValiditySeconds(60*60*24)
                    .secret(passwordEncoder.encode("topsecret")).refreshTokenValiditySeconds(0);
        }

        /**
         * We here defines the security constraints on the token endpoint.
         * We set it up to isAuthenticated, which returns true if the user is not anonymous
         * @param security the AuthorizationServerSecurityConfigurer.
         * @throws Exception
         */
        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security
                    .checkTokenAccess("isAuthenticated()")
                    .allowFormAuthenticationForClients();

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Arrays.asList("*"));
            config.applyPermitDefaultValues();


            // add allow-origin to the headers
            config.addAllowedHeader("*");

            source.registerCorsConfiguration("/oauth/token", config);
            CorsFilter filter = new CorsFilter(source);
            security.addTokenEndpointAuthenticationFilter(filter);
        }
    }
}