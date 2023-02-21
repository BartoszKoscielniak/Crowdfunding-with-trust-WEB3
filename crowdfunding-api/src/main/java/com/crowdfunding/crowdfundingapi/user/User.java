package com.crowdfunding.crowdfundingapi.user;

import com.crowdfunding.crowdfundingapi.poll.vote.Vote;
import com.crowdfunding.crowdfundingapi.support.CollUserRelation;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false)
    private Long id;
    private String publicAddress;
    private String name;
    private String lastname;
    private String email;
    private String phoneNumber;
    @JsonIgnore
    byte[] privateKey;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private boolean isAccountNonExpired = true;
    @JsonIgnore
    private boolean isCredentialsNonExpired = true;
    @JsonIgnore
    private boolean isEnabled = true;
    @JsonIgnore
    private boolean isAccountNonLocked = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<CollUserRelation> collUserRelations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Vote> votes;

    @Enumerated
    private UserRole userRole;
    public User( ) { }

    public User(String publicAddress, String name, String lastname, String email, String phoneNumber, byte[] privateKey, String password, UserRole userRole) {
        this.publicAddress = publicAddress;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.privateKey = privateKey;
        this.password = password;
        this.userRole = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities( ) {
        return userRole.getGrantedAuthorities();
    }

    @Override
    public String getPassword( ) {
        return password;
    }

    @Override
    public String getUsername( ) {
        return publicAddress;
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
