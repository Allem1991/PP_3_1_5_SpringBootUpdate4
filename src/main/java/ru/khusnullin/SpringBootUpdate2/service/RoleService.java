package ru.khusnullin.SpringBootUpdate2.service;

import ru.khusnullin.SpringBootUpdate2.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getRoles();
    void saveRole(Role role);
    void removeRoleById(Long id);
    Role getRoleByName(String name);
    Role getRoleById(Long id);
}
