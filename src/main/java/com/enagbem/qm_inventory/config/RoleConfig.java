package com.enagbem.qm_inventory.config;

import com.enagbem.qm_inventory.model.Role;
import com.enagbem.qm_inventory.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleConfig {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            createRoleIfNotExists(roleRepository, "ROLE_USER");
            createRoleIfNotExists(roleRepository, "ROLE_ADMIN");
            createRoleIfNotExists(roleRepository, "ROLE_MODERATOR");
        };
    }

    private void createRoleIfNotExists(RoleRepository roleRepository, String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = Role.builder()
                    .name(roleName)
                    .build();
            roleRepository.save(role);
        }
    }
}