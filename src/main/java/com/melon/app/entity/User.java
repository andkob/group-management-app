package com.melon.app.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"user\"")  // Escape the table name
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String email;

    private String passwordHash;

    @ManyToMany
    @JoinTable(
      name = "user_organization", 
      joinColumns = @JoinColumn(name = "user_id"), 
      inverseJoinColumns = @JoinColumn(name = "organization_id"))
    private Set<Organization> organizations;

    public void setEmail(String email) {
        this.email = email;
    }

    // TODO - Implement Hashing either here or somewhere else
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
