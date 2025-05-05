package com.ascoop.taskmanager.config;

import com.ascoop.taskmanager.model.User;
import com.ascoop.taskmanager.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final RsaKeyConfig rsaKeyConfig;

   // private final OAuth2LoginSuccessHandler successHandler;
    @Bean
    public AuthenticationManager authenticationManager( UserDetailsService userDetailsService,PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder( passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((requests) -> {

                    requests.requestMatchers("auth/**","register","hello").permitAll().
                            anyRequest().authenticated();

                })
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable).
                sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).//rendre de la session sans etat pour les apis restful
                /* oauth2Login(oauth2Login -> {
                     oauth2Login.successHandler(successHandler);
                }).*/
                        oauth2ResourceServer(oauth2->oauth2.jwt(Customizer.withDefaults())).
                build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration config= new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        config.setAllowCredentials(true);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeyConfig.publicKey()).privateKey(rsaKeyConfig.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        //decoder custom avec rsa
       /// JwtDecoder customDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyConfig.publicKey()).build();
        // Google decoder
       /* NimbusJwtDecoder googleDecoder = JwtDecoders.fromIssuerLocation("https://accounts.google.com");

        return token -> {
                String issuer = extractIssuer(token);
                if ("https://accounts.google.com".equals(issuer)){
                    return googleDecoder.decode(token);
                }else {
                    return customDecoder.decode(token);
                }
        };*/
      return   NimbusJwtDecoder.withPublicKey(rsaKeyConfig.publicKey()).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    public static String extractIssuer(String token) {
        String[] chunks = token.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode json = mapper.readTree(payload);
            return json.get("iss").asText();
        } catch (IOException e) {
            throw new RuntimeException("Invalid JWT", e);
        }
    }
  /*  @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(UserRepository userRepository) {
        return userRequest -> {
            OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");
            String googleId = oAuth2User.getAttribute("sub"); // identifiant unique chez Google
            System.out.println("OAuth2UserService---------");
            System.out.println(Optional.ofNullable(oAuth2User.getAttribute("email")));
            System.out.println("OAuth2UserService---------");
          *//*  User user = userRepository.findByEmail(email).orElseGet(() -> {
                // Première connexion : on crée l'utilisateur
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setUsername(name);
                newUser.setProviderId(googleId); // pour identifier Google
                return userRepository.save(newUser);
            });*//*

            // Authentification réussie, on renvoie l'utilisateur Spring
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    oAuth2User.getAttributes(),
                    "sub" // clé de l'attribut principal
            );
        };
    }*/

}
