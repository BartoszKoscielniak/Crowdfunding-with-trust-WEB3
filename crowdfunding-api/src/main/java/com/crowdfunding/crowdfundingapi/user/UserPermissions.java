package com.crowdfunding.crowdfundingapi.user;

public enum UserPermissions {
    USER_CREATE("user:create"),
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),
    ADMIN_CREATE("ADMINr:create"),
    ADMIN_READ("ADMIN:read"),
    ADMIN_UPDATE("ADMINr:update"),
    ADMIN_DELETE("ADMIN:delete");

    private final String permission;

    UserPermissions(String permission){
        this.permission = permission;
    }

    public String getPermission( ) {
        return permission;
    }
}
