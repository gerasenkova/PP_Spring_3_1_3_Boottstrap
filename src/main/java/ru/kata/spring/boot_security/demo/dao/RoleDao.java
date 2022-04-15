package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.HashSet;
import java.util.List;

public interface RoleDao {
    public List<Role> getAllRoles();

    public Role getRoleByName(String name);


}
