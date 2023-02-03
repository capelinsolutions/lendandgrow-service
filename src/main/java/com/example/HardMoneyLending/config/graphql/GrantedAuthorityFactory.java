package com.example.HardMoneyLending.config.graphql;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GrantedAuthorityFactory {

    public static List<GrantedAuthority> getAuthoritiesFrom(String userRoles) {
        if (StringUtils.isBlank(userRoles)) {
            return new ArrayList<>();
        }

        return Arrays.asList(userRoles.split(","))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
