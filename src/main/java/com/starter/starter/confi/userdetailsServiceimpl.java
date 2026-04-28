package com.starter.starter.confi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.starter.starter.dao.userRepo;
import com.starter.starter.entites.user;



@Service
public class userdetailsServiceimpl implements UserDetailsService {

    @Autowired
    private userRepo userrepo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        user user = userrepo.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException(
                "User not found with email: " + username
            );
        }

        return new customuserdetail(user);
    }
}

// now for the 3rd step we have to configure spring security to use our custom user detail service for authentication process
// so we give the userdetailsServiceimpl class in the security configuration file (securityconfig.java) beacuse it already contain the loadUserByUsername method which is used by spring security for authentication process as well as the customuserdetail class is also used to represent the user details.
