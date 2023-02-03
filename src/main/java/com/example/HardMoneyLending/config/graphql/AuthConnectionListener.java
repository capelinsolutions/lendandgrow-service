package com.example.HardMoneyLending.config.graphql;

import graphql.kickstart.execution.subscriptions.SubscriptionSession;
import graphql.kickstart.execution.subscriptions.apollo.ApolloSubscriptionConnectionListener;
import graphql.kickstart.execution.subscriptions.apollo.OperationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Component
public class AuthConnectionListener implements ApolloSubscriptionConnectionListener{

    public static final String AUTHENTICATION = "AUTHENTICATION";
    public static final String AUTHORIZATION = "Authorization";
    public static final String HEADER_USER_ID = "user_id";
    public static final String HEADER_ROLES = "roles";

    @Override
    public void onConnect(SubscriptionSession session, OperationMessage message) {
        log.info("onConnect with payload {}", message.getPayload());

        Map<String, String> payload = (Map<String, String>) message.getPayload();

        // Get the user id, roles (or JWT etc) and perform authentication / rejection here
        String accessToken = payload.get(AUTHORIZATION);
        String userId = payload.get(HEADER_USER_ID);
        String userRoles = payload.get(HEADER_ROLES);
        Authentication token = new PreAuthenticatedAuthenticationToken(
                userId,
                accessToken,
                GrantedAuthorityFactory.getAuthoritiesFrom(userRoles)
        );
        session.getUserProperties().put(AUTHENTICATION, token);
    }

    @Override
    public void onStart(SubscriptionSession session, OperationMessage message) {
        log.info("onStart with payload {}", message.getPayload());
        Authentication authentication = (Authentication) session.getUserProperties().get(AUTHENTICATION);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void onStop(SubscriptionSession session, OperationMessage message) {
        log.info("onStop with payload {}", message.getPayload());
    }

    @Override
    public void onTerminate(SubscriptionSession session, OperationMessage message) {
        log.info("onTerminate with payload {}", message.getPayload());
    }
}
