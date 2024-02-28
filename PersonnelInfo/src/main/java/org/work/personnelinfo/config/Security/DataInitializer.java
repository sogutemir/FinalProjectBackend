package org.work.personnelinfo.config.Security;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.work.personnelinfo.admin.Service.UserService;
import org.work.personnelinfo.admin.repository.UserRepository;

@Component
public class DataInitializer {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        userService.initializeUsers();
    }
}

