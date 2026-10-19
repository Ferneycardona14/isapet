package com.isapet.security;

import com.isapet.entity.AppUser;
import com.isapet.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AdminUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales inválidas."));

        return User.withUsername(appUser.getUsername())
                .password(appUser.getPasswordHash())
                .disabled(!appUser.isEnabled())
                .roles(appUser.getRoles().stream().map(role -> role.getName()).toArray(String[]::new))
                .build();
    }
}
