package com.starter.starter.confi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Homeconfiguration {
  

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

  @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .csrf(csrf -> csrf.disable())

        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/home", "/about", "/signup", "/do_register", "/css/**", "/img/**").permitAll()


            .requestMatchers("/user/**").hasRole("USER")
            .requestMatchers("/admin/**").hasRole("ADMIN")

            .anyRequest().authenticated()
        )

        .formLogin(form -> form
            .loginPage("/login")              // custom login page
            .loginProcessingUrl("/do_login")  // POST
            .usernameParameter("email")
            .passwordParameter("password")
            .defaultSuccessUrl("/user/index", true)
            .permitAll()
        )
        // here in the login page first we have the default login page but it is not for everyone only those can access who are not authenticated there it as a do login page to authenticate the user which asked for email and password and after successful authentication it will redirect to /user/index page
         // and this is only for the users having role USER and for admin we have to create another login page if required otherwise we can use the same login page for admin also by changing the default success url to /admin/index for admin role in the same formLogin method by checking the role of the user after successful authentication but here we are using separate login pages for user and admin for better understanding
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
        );
        // same as login here also we have custom logout url and after successful logout it will redirect to login page with a logout parameter

    return http.build();
}

}


// this page is not from durgesh sir's course due to newer version of spring security rest all the functionality is same as taught in the course.
