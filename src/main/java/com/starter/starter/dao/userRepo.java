package com.starter.starter.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.starter.starter.entites.user;


public interface userRepo extends JpaRepository<user, Integer> {


    @Query("select u from user u where u.email = :email")
    public user findByEmail(@Param("email") String email);

    // here we have to find  the user by email therefore we are writing a custom query using @Query annotation
    // but email is static therefore we have to use @Param to bind the email parameter to the query
}
