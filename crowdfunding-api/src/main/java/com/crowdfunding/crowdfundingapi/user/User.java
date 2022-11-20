package com.crowdfunding.crowdfundingapi.user;

import com.crowdfunding.crowdfundingapi.poll.vote.Vote;
import com.crowdfunding.crowdfundingapi.support.CollUserRelation;
import lombok.AllArgsConstructor;
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
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false)
    private Long id;
    private String publicAddress;
    private String password;
    private String nonce;
    private boolean isAccountNonExpired;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
    private boolean isAccountNonLocked;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<CollUserRelation> collUserRelations;

    @OneToMany(mappedBy = "user")
    private Set<Vote> pools;

    @Enumerated
    private UserRole userRole;
    public User( ) {

    }

    public User(String publicAddress, String password, String nonce) {
        this.publicAddress = publicAddress;
        this.password = password;
        this.nonce = nonce;
        this.isAccountNonExpired = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
        this.isAccountNonLocked = true;
        this.userRole = UserRole.USER;
    }

    public User(String publicAddress, String password, String nonce, UserRole userRole, boolean isAccountNonExpired, boolean isCredentialsNonExpired, boolean isEnabled, boolean isAccountNonLocked) {
        this.publicAddress = publicAddress;
        this.password = password;
        this.nonce = nonce;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.isAccountNonLocked = isAccountNonLocked;
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
