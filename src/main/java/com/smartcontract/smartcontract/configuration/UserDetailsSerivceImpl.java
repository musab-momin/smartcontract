package com.smartcontract.smartcontract.configuration;

import com.smartcontract.smartcontract.dao.RegisterRepo;
import com.smartcontract.smartcontract.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsSerivceImpl implements UserDetailsService 
{
    @Autowired
    private RegisterRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException 
    {
        User user = this.repo.getUserByName(email);
        if(user==null)
        {
            throw new UsernameNotFoundException("User not found");
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user);       
        return customUserDetails;
    }
    
    
}
