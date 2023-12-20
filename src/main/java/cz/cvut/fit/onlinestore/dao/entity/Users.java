package cz.cvut.fit.onlinestore.dao.entity;

import java.util.HashSet;
import java.util.Set;

// Users because 'user' is reserved word in sql
public class Users {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String address;
    private String password;
    private Set<Orders> orders = new HashSet<>();
}
