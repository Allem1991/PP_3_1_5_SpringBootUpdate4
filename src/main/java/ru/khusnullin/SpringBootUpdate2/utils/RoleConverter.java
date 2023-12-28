package ru.khusnullin.SpringBootUpdate2.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import ru.khusnullin.SpringBootUpdate2.model.Role;
import ru.khusnullin.SpringBootUpdate2.service.RoleService;

import java.util.Locale;

@Component
public class RoleConverter implements Formatter<Role> {

    private final RoleService roleService;

    @Autowired
    public RoleConverter(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Role parse(String roleName, Locale locale) {
        return roleService.getRoleByName(roleName);
    }

    @Override
    public String print(Role role, Locale locale) {
        return role.getName();
    }
}
