package com.TechPro.SpringBootStudy.basic_authentication;


import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.TechPro.SpringBootStudy.basic_authentication.ApplicationUserPermission.STUDENT_READ;
import static com.TechPro.SpringBootStudy.basic_authentication.ApplicationUserPermission.STUDENT_WRITE;

public enum ApplicationUserRoles {

    USER(Sets.newHashSet(STUDENT_READ)), ADMIN(Sets.newHashSet(STUDENT_WRITE, STUDENT_READ));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRoles(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> izinleriGetirenMetod() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> izinler() {

        Set<SimpleGrantedAuthority> onaylananIzinler = izinleriGetirenMetod()
                .stream()
                .map(t -> new SimpleGrantedAuthority(t.getPermission()))
                .collect(Collectors.toSet());

        onaylananIzinler.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return onaylananIzinler;
    }


}
