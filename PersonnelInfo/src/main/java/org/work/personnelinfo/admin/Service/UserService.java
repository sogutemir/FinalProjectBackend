package org.work.personnelinfo.admin.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.work.personnelinfo.admin.dto.UserDTO;
import org.work.personnelinfo.admin.mapper.UserEntityMapper;
import org.work.personnelinfo.admin.model.Role;
import org.work.personnelinfo.admin.model.UserEntity;
import org.work.personnelinfo.admin.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.work.personnelinfo.personel.model.PersonelEntity;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserEntityMapper userEntityMapper;

    public UserService(UserRepository userRepository, UserEntityMapper userEntityMapper,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
        this.passwordEncoder = passwordEncoder;
    }
    public void initializeUsers() {
        if (userRepository.count() == 0) {
            createUsers();
        }
    }

    public void createUsers() {
        UserEntity admin = createUserEntity("admin", "admin123", Role.ADMIN);
        UserEntity superUser = createUserEntity("superuser", "superuser123", Role.SUPERUSER);
        UserEntity user = createUserEntity("user", "user123", Role.USER);
        UserEntity authUser = createUserEntity("authuser", "authuser123", Role.AUTHORIZEDUSER);

        userRepository.saveAll(Arrays.asList(admin, superUser, user, authUser));
    }

    private UserEntity createUserEntity(String username, String password, Role role) {
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
        user.setRoles(new HashSet<>(Arrays.asList(Role.USER)));
        return userRepository.save(user);
    }

    public UserDTO updateUserRoles(Long id, Set<Role> newRoles) {
        return userRepository.findById(id)
                .map(existingUserEntity -> {
                    existingUserEntity.setRoles(newRoles);
                    UserEntity savedUser = userRepository.save(existingUserEntity);
                    return userEntityMapper.toDto(savedUser);
                })
                .orElseThrow(() -> new IllegalArgumentException("No user found with id " + id));
    }






}

