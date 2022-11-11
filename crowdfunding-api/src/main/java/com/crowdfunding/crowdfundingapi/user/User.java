package com.crowdfunding.crowdfundingapi.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String publicKey;
    private String privateKey;
    private boolean isAccountNonExpired;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
    private boolean isAccountNonLocked;
    @Enumerated
    private UserRole userRole;
    public User( ) {

    }

    public User(String publicKey, String privateKey, UserRole userRole, boolean isAccountNonExpired, boolean isCredentialsNonExpired, boolean isEnabled, boolean isAccountNonLocked) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.isAccountNonLocked = isAccountNonLocked;
        this.userRole = userRole;
    }

    public Long getId( ) {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublicKey( ) {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey( ) {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities( ) {
        return userRole.getGrantedAuthorities();
    }

    @Override
    public String getPassword( ) {
        return privateKey;
    }

    @Override
    public String getUsername( ) {
        return publicKey;
    }

    @Override
    public boolean isAccountNonExpired( ) {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked( ) {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired( ) {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled( ) {
        return isEnabled;
    }
}
