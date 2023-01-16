package com.crowdfunding.crowdfundingapi.user;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.crowdfunding.crowdfundingapi.user.UserPermissions.*;

public enum UserRole {
    USER(Sets.newHashSet(USER_CREATE, USER_DELETE, USER_READ, USER_UPDATE)),
    ADMIN(Sets.newHashSet(ADMIN_CREATE, ADMIN_DELETE, ADMIN_READ, ADMIN_UPDATE));

    private final Set<UserPermissions> permissions;

    UserRole(Set<UserPermissions> permissions){
        this.permissions = permissions;
    }

    public Set<UserPermissions> getPermissions( ) {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return permissions;
    }
}
