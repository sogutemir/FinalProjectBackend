package org.work.personnelinfo.config.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.work.personnelinfo.admin.Service.UserService;
import org.work.personnelinfo.admin.repository.UserRepository;

@Component
public class DataInitializer {


    private final UserRepository userRepository;
    private final UserService userService;

    public DataInitializer(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userRepository.count() == 0) {
            userService.createUsers();
        }
    }
}

