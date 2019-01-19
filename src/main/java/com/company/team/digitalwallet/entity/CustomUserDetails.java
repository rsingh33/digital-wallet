package com.company.team.digitalwallet.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails extends User implements UserDetails {

    public CustomUserDetails(final User user){
        super(user);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

    List<SimpleGrantedAuthority> list =    getRoles().stream()
                .map( role -> new SimpleGrantedAuthority(role.getRole()))
                .collect((Collectors.toList()));

        return list;
    }

    @Override
    public String getPassword() {
        return super.getPin();
    }

    @Override
    public String getUsername() {
        return super.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
