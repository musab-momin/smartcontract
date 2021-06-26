package com.smartcontract.smartcontract.dao;



import com.smartcontract.smartcontract.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface contatctRepo extends JpaRepository<Contact, Integer> 
{
    @Query("from Contact as c where c.user.id=:userId")
    public Page<Contact> getContactById(@Param("userId") int userId, Pageable pageable);
    
}
