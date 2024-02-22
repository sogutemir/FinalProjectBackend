package org.work.personnelinfo.admin.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.work.personnelinfo.admin.model.UserEntity;
import org.work.personnelinfo.admin.model.RoleEntity;
import org.work.personnelinfo.admin.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import org.work.personnelinfo.admin.repository.RoleRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUsers() {
        RoleEntity adminRole = ensureRoleExists("ADMIN");
        RoleEntity superUserRole = ensureRoleExists("SUPERUSER");
        RoleEntity userRole = ensureRoleExists("USER");

        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRoles(new HashSet<>(Arrays.asList(adminRole)));

        UserEntity superUser = new UserEntity();
        superUser.setUsername("superuser");
        superUser.setPassword(passwordEncoder.encode("superuser123"));
        superUser.setRoles(new HashSet<>(Arrays.asList(superUserRole)));

        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));

        userRepository.save(admin);
        userRepository.save(superUser);
        userRepository.save(user);
    }

    private RoleEntity ensureRoleExists(String roleName) {
        return roleRepository.findByName(roleName).orElseGet(() -> {
            RoleEntity newRole = new RoleEntity(roleName);
            roleRepository.save(newRole);
            return newRole;
        });
    }

}

