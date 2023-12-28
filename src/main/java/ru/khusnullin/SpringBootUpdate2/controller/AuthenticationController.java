package ru.khusnullin.SpringBootUpdate2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.khusnullin.SpringBootUpdate2.model.Role;
import ru.khusnullin.SpringBootUpdate2.model.User;
import ru.khusnullin.SpringBootUpdate2.service.RoleService;
import ru.khusnullin.SpringBootUpdate2.service.UserService;
import ru.khusnullin.SpringBootUpdate2.utils.RoleConverter;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AuthenticationController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addCustomFormatter(new RoleConverter(roleService));
    }

    @GetMapping("/login")
    public String loginPage() {
        return "authentication/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user, ModelMap model) {
        Role roleUser = roleService.getRoleByName("ROLE_USER");
        model.addAttribute("roleUser", roleUser);
        return "authentication/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors() || userService.getUserByEmail(user.getEmail()) != null) {
            Role roleUser = roleService.getRoleByName("ROLE_USER");
            model.addAttribute("roleUser", roleUser);
            return "authentication/registration";
        }
        userService.addUser(user);
        return "redirect:/auth/login";
    }
}
