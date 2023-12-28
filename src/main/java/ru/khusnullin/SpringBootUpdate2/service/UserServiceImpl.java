package ru.khusnullin.SpringBootUpdate2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khusnullin.SpringBootUpdate2.model.Role;
import ru.khusnullin.SpringBootUpdate2.model.User;
import ru.khusnullin.SpringBootUpdate2.repository.UserRepository;
import ru.khusnullin.SpringBootUpdate2.utils.UserNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        Optional<User> user= userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        Optional<User> userById = userRepository.findById(user.getId());
        if (userById.isPresent()) {
            User userOld = userById.get();
            userOld.setId(user.getId());
            userOld.setEmail(user.getEmail());
            userOld.setName(user.getName());
            userOld.setLastname(user.getLastname());
            userOld.setAge(user.getAge());
            userOld.setPassword(passwordEncoder.encode(user.getPassword()));
            userOld.setRoles(user.getRoles());
            userRepository.save(userOld);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with %s email not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

}
