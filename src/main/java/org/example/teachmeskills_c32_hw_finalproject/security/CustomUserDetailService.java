package org.example.teachmeskills_c32_hw_finalproject.security;


import org.example.teachmeskills_c32_hw_finalproject.model.users.Security;
import org.example.teachmeskills_c32_hw_finalproject.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final SecurityRepository securityRepository;

    @Autowired
    public CustomUserDetailService(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Security> securityOptional = securityRepository.findByLogin(username);
        if (securityOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        Security security = securityOptional.get();

        return User
                .withUsername(security.getLogin())
                .password(security.getPassword())
                .roles(security.getRole().toString())
                .build();
    }
}