package cz.cvut.fit.onlinestore.dao.entity;

import java.util.HashSet;
import java.util.Set;

public class Product {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String category;
    private Set<Comment> comments = new HashSet<>();
    private Set<Orders> orders = new HashSet<>();

}
