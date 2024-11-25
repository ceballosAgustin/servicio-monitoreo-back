package com.techforb.aceballos.servicio_monitoreo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IJpaUserDetailsService extends UserDetailsService{
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
