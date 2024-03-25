package com.spring.project.users.userinfo;

import jakarta.persistence.*;

import java.util.Collection;
import com.spring.project.users.*;

@Entity
@Table(name = "user_types")
public class Role {
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @ManyToOne(mappedBy = "roles")
    private Collection<User> users;

}