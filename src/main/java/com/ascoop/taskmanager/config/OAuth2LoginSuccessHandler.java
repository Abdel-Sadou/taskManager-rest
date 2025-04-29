package com.ascoop.taskmanager.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtEncoder jwtEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String username = oauthToken.getPrincipal().getAttribute("email");
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(username)
            .claim("roles", authorities)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(12, ChronoUnit.HOURS))
            .issuer("task-manager")
            .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        // Renvoie le token au front via JSON (ou redirige selon le besoin)
        response.setContentType("application/json");
        response.getWriter().write("{\"access_token\": \"" + token + "\"}");
    }
}
