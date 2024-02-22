package org.work.personnelinfo.admin.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.work.personnelinfo.admin.dto.UserDTO;
import org.work.personnelinfo.admin.model.RoleEntity;
import org.work.personnelinfo.admin.model.UserEntity;
import org.work.personnelinfo.admin.repository.UserRepository;
import org.work.personnelinfo.config.Security.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(UserDTO entity){
        UserEntity account = userRepository.findByUsername(entity.getUsername());
        if (account != null && passwordEncoder.matches(entity.getPassword(), account.getPassword())) {
            List<String> roles = account.getRoles().stream()
                    .map(RoleEntity::getName)
                    .collect(Collectors.toList());
            return jwtUtil.generateToken(entity.getUsername(), roles);
        }
        return null;
    }

}
