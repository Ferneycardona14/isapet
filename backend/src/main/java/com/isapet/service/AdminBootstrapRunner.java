package com.isapet.service;

import com.isapet.entity.AppRole;
import com.isapet.entity.AppUser;
import com.isapet.repository.AppRoleRepository;
import com.isapet.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Set;

@Component
public class AdminBootstrapRunner implements ApplicationRunner {

    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final boolean enabled;
    private final String username;
    private final String password;

    public AdminBootstrapRunner(
            AppUserRepository appUserRepository,
            AppRoleRepository appRoleRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.security.bootstrap-admin.enabled:false}") boolean enabled,
            @Value("${BOOTSTRAP_ADMIN_USERNAME:}") String username,
            @Value("${BOOTSTRAP_ADMIN_PASSWORD:}") String password) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.enabled = enabled;
        this.username = username;
        this.password = password;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!enabled || appUserRepository.existsByRoles_Name("ADMIN")) {
            return;
        }
        if (username.isBlank() || password.isBlank()
                || password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalStateException(
                    "El bootstrap de ADMIN requiere usuario y contraseña; la contraseña no puede superar 72 bytes UTF-8.");
        }

        String normalizedUsername = username.trim();
        if (normalizedUsername.length() > 100) {
            throw new IllegalStateException("El usuario de bootstrap no puede superar 100 caracteres.");
        }
        if (appUserRepository.findByUsernameIgnoreCase(normalizedUsername).isPresent()) {
            throw new IllegalStateException(
                    "El usuario configurado para bootstrap ya existe y no tiene el rol ADMIN.");
        }
        AppRole adminRole = appRoleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException("No está inicializado el rol ADMIN."));

        appUserRepository.save(new AppUser(
                normalizedUsername,
                passwordEncoder.encode(password),
                Set.of(adminRole)));
    }
}
