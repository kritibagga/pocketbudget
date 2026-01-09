package com.pocketbudget.pocketbudget.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pocketbudget.pocketbudget.user.User;
import com.pocketbudget.pocketbudget.user.UserRepository;


@Service
public class CustomUserDetailsService  implements UserDetailsService{


    private final UserRepository users;
    public CustomUserDetailsService(UserRepository users){
        this.users=users;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        Long userId = Long.parseLong(username);
        User user= users.findById(userId)
                    .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
            user.getId().toString(),
            user.getPasswordHash(),
            List.of(new SimpleGrantedAuthority("Role_" + user.getRole().name()))

        );

    }


}
