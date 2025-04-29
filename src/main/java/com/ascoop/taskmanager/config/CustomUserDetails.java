package com.ascoop.taskmanager.config;

import com.ascoop.taskmanager.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private  Collection<? extends GrantedAuthority> authorities;
    private final boolean isEnabled;

       public CustomUserDetails(User user) {
            this.username = user.getUsername(); // ou getUsername()
            this.password = user.getPassword();
            this.authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
            this.isEnabled = user.isEnabled();
        }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return  password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
