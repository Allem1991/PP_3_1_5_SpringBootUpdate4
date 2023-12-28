package ru.khusnullin.SpringBootUpdate2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khusnullin.SpringBootUpdate2.model.Role;
import ru.khusnullin.SpringBootUpdate2.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Role> getRoles() {
       return roleRepository.findAll();
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void removeRoleById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleByName(String name) {
        return roleRepository.getRoleByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            return role.get();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
