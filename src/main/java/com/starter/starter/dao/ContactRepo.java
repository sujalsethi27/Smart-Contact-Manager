package com.starter.starter.dao;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.starter.starter.entites.contact;



public interface ContactRepo extends JpaRepository<contact , Integer>  {
// pegination

@Query(" select c from contact c where c.user.id = :userId")
// this pageable interface store two information that is contacts per page and current page we are on
public Page<contact> findContactsByUser(@Param("userId") int userId , Pageable pageable);

  //here we ran a query so that we can fetch the contact of that specific user  that is logged in at that time
//searching contacts by name

Page<contact> findByUserIdAndNameContaining(
        int userId,
        String name,
        Pageable pageable
);
}

