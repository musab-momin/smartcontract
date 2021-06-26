package com.smartcontract.smartcontract.dao;

import com.smartcontract.smartcontract.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegisterRepo extends JpaRepository<User, Integer>
{
    @Query("select u from User u where u.email=:email")
    public User getUserByName(@Param("email") String email);
    
}
