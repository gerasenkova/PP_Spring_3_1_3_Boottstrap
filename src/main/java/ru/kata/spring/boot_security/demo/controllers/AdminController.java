package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String printUsers(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user, Model model) {
        model.addAttribute("user", userService.findByEmail(user.getUsername()));
        model.addAttribute("users", userService.userList());
        return "user";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("newUser") User user, HttpServletRequest request) {
        Set <Role> roles=new HashSet<>();
        String[] userRoles=request.getParameterValues("role1");
        for(String roleId : userRoles){
            if(Long.parseLong(roleId)==2L){
                roles.add(roleService.getRoleById(2L));
            }
            if(Long.parseLong(roleId)==1L){
                roles.add(roleService.getRoleById(1L));
            }
        }
        user.setRoles(roles);
        userService.saveUser(user);
        return "redirect:/admin";
    }



    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String edit(ModelMap model, @PathVariable("id") Long id) {
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", userService.getById(id));
        return "edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") User user,
                         @PathVariable("id") Long id, HttpServletRequest request) {
        Set <Role> roles=new HashSet<>();
        String[] userRoles=request.getParameterValues("role1");
        for(String roleId : userRoles){
            if(Long.parseLong(roleId)==2L){
                roles.add(roleService.getRoleById(2L));
            }
            if(Long.parseLong(roleId)==1L){
                roles.add(roleService.getRoleById(1L));
            }
        }
        user.setRoles(roles);
        userService.updateUser(user);
        return "redirect:/admin";
    }
}


