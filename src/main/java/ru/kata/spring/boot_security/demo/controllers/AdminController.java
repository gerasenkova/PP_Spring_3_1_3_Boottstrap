package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
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
    public String allUsers(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("users", userService.userList());
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin";
    }

    @PostMapping
    public String add(@ModelAttribute("user") User user,
                      @RequestParam(value = "nameRoles") String[] roles) {
        Set<Role> roles1 = new HashSet<>();
        for (String role : roles) {
            roles1.add(roleService.getRoleByName(role));
        }
        user.setRoles(roles1);
        userService.saveUser(user);
        return "redirect:/admin/";
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }


    @GetMapping("/{id}/edit")
    public String edit(@ModelAttribute("user") User user,
                       ModelMap model,
                       @PathVariable("id") int id,
                       @RequestParam(value = "editRoles") String[] roles) {
       Set<Role> roles1=new HashSet<>();
       for(String role:roles){
           roles1.add(roleService.getRoleByName(role));
       }
       user.setRoles(roles1);
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", userService.getById(id));
        return "admin";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user,
                         @PathVariable("id") int id,
                         @RequestParam(value = "editRoles") String[] roles) {
        Set<Role> roles1=new HashSet<>();
        for(String role:roles){
            roles1.add(roleService.getRoleByName(role));
        }
        user.setRoles(roles1);
        userService.updateUser(user);
        return "redirect:/admin/";
    }
}


