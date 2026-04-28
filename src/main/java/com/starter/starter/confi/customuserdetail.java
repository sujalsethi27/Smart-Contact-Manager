package com.starter.starter.confi;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.starter.starter.entites.user;

public class customuserdetail implements UserDetails {

    private user user;

    public customuserdetail(user user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // LOGIN BY EMAIL
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
        return user.isEnabled();
    }
}

// here we got all the methods from UserDetails(predefined) interface which we have to implement in our custom user detail class

// UserDetails → ID card of a person

// UserDetailsService → Security office that fetches the ID card from records

// For authentication process we have to do 3 steps that is
// 1. Create a class that implements UserDetails interface (customuserdetail.java) → this class will represent the user details
// 2. Create a class that implements UserDetailsService interface (customuserdetailservice.java) → this class will fetch the user details from database using userRepo
// 3. Configure Spring Security to use our custom UserDetailsService (securityconfig.java)