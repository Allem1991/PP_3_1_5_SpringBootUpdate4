package ru.khusnullin.SpringBootUpdate2.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.khusnullin.SpringBootUpdate2.model.Role;
import ru.khusnullin.SpringBootUpdate2.model.User;
import ru.khusnullin.SpringBootUpdate2.service.RoleService;
import ru.khusnullin.SpringBootUpdate2.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
public class DbInit {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public DbInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void initialization() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");

        roleService.saveRole(roleAdmin);
        roleService.saveRole(roleUser);

        User admin = new User("admin@mail.ru", "123456789Qr@", "Thomas", "Lane", 34L,
                Set.of(roleService.getRoleByName("ROLE_ADMIN"), roleService.getRoleByName("ROLE_USER")));

        userService.addUser(admin);

        User user = new User("user@mail.ru", "1234567Qr@", "Андрей", "Волков", 23L,
                Set.of(roleService.getRoleByName("ROLE_USER")));

        userService.addUser(user);

    }
}
