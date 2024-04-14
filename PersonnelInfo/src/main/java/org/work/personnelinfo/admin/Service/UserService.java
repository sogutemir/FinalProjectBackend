package org.work.personnelinfo.admin.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.work.personnelinfo.admin.dto.UserDTO;
import org.work.personnelinfo.admin.mapper.UserEntityMapper;
import org.work.personnelinfo.admin.model.UserEntity;
import org.work.personnelinfo.admin.model.RoleEntity;
import org.work.personnelinfo.admin.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import org.work.personnelinfo.admin.repository.RoleRepository;
import org.work.personnelinfo.personel.model.PersonelEntity;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserEntityMapper userEntityMapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserEntityMapper userEntityMapper ,BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userEntityMapper = userEntityMapper;
        this.passwordEncoder = passwordEncoder;
    }
    public void initializeUsers() {
        if (userRepository.count() == 0) {
            createUsers();
        }
    }

    public void createUsers() {
        RoleEntity adminRole = ensureRoleExists("ADMIN");
        RoleEntity superUserRole = ensureRoleExists("SUPERUSER");
        RoleEntity userRole = ensureRoleExists("USER");

        UserEntity admin = createUserEntity("admin", "admin123", adminRole);
        UserEntity superUser = createUserEntity("superuser", "superuser123", superUserRole);
        UserEntity user = createUserEntity("user", "user123", userRole);

        userRepository.saveAll(Arrays.asList(admin, superUser, user));
    }

    private UserEntity createUserEntity(String username, String password, RoleEntity role) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(new HashSet<>(Arrays.asList(role)));
        return user;
    }

    public UserEntity createUserEntity(PersonelEntity personelEntity, String username, String password) {
        UserEntity user = new UserEntity();
        user.setPersonel(personelEntity);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        RoleEntity userRole = ensureRoleExists("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));


        return userRepository.save(user);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        UserEntity newUserEntity = userEntityMapper.toEntity(userDTO);

        return userRepository.findById(id)
                .map(existingUserEntity -> {
                    userEntityMapper.partialUpdate(userDTO, existingUserEntity);
                    UserEntity updatedUserEntity = userRepository.save(existingUserEntity);
                    return userEntityMapper.toDto(updatedUserEntity);
                })
                .orElseGet(() -> {
                    newUserEntity.setId(id);
                    UserEntity savedUserEntity = userRepository.save(newUserEntity);
                    return userEntityMapper.toDto(savedUserEntity);
                });
    }


    private RoleEntity ensureRoleExists(String roleName) {
        return roleRepository.findByName(roleName).orElseGet(() -> {
            RoleEntity newRole = new RoleEntity(roleName);
            roleRepository.save(newRole);
            return newRole;
        });
    }


}

