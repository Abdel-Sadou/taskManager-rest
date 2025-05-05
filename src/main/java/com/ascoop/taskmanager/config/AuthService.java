package com.ascoop.taskmanager.config;

import com.ascoop.taskmanager.dto.UserDTO;
import com.ascoop.taskmanager.model.User;
import com.ascoop.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
     private final UserRepository userRepository;



    public Map<String, Object> generateAccessToken(String username, String password) {
        try {
         //  String encryptedPassword = SiiSecurity.cryptePwdNew(username, password.trim()).trim();
           Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String subject = authentication.getName();
            System.out.println(subject);
            /*String scope =
            authentication.getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));*/
            return buildAccessToken(subject, false);
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (CredentialsExpiredException e) {
            throw new CredentialsExpiredException(e.getMessage());
        } catch (AccountExpiredException e) {
            throw new AccountExpiredException(e.getMessage());
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        }catch (Exception e) {
            System.out.println("*****************************************************");
            System.out.println(e.getMessage());
            System.out.println("*****************************************************");
            e.printStackTrace();
            throw new BadCredentialsException(e.getMessage());
        }
    }

    public Map<String, Object> buildAccessToken(String subject , boolean isGoogle) {
        Instant instant = Instant.now();
        int accessTokenTimeOut = 12;
        Instant tokenExpireAt = instant.plus(accessTokenTimeOut, ChronoUnit.HOURS);
        UserDTO userDTO =  isGoogle?userRepository.findByEmail(subject).map(UserDTO::new).get(): userRepository.findByUsername(subject).map(UserDTO::new).get();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(instant)
                .expiresAt(tokenExpireAt)
                .issuer("TAsk Manager")
                .claim("roles", userDTO.getRole())
                .claim("user",userDTO)
                .build();
        String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        Map<String, Object> token = new HashMap<>();
        token.put("access_token", jwtAccessToken);
        token.put("access_generate_at", String.valueOf(instant));
        token.put("access_expires_in_hours", accessTokenTimeOut);
        return token;
    }



}
