package com.scm.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;
import java.util.stream.Collectors;

@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails{
    @Id
    private String userId;
    @Column(name="user_name", nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Getter(value = AccessLevel.NONE)
    private String password;
    @Column(length = 10000)
    private String about;
    private String profilePic;
    private String phoneNumber;
    //information
    @Getter(value = AccessLevel.NONE)
    private boolean enabled=true;
    private boolean emailVerified=false;
    private boolean phoneVerified = false;

    @Enumerated(value = EnumType.STRING)
    //SELF, Google, Facebook, Twiter, LinkedIN, GitHub
    private Providers provider=Providers.SELF;
    private String providerUserId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true )
    private List<Contact> contacts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> rolesList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //List of roles [USER, ADMIN,..]
        //collection of SimpleGrantedAuthority [roles{USER, ADMIN,..}]
        Collection<SimpleGrantedAuthority> roles =rolesList.stream().map(role-> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
    }

    //In this project email id is our username 
    @Override
    public String getUsername() {
        return this.email;    
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

}
