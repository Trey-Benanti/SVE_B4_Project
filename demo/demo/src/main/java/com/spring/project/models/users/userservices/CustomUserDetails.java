package com.spring.project.models.users.userservices;
import java.util.ArrayList;
import java.util.Collection;
 
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.spring.project.models.users.User;
 
public class CustomUserDetails implements UserDetails {
    String rolePrefix = "ROLE_";

    private User user;
     
    public CustomUserDetails(User user) {
        this.user = user;
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

        list.add(this.user.role);

        return list;
    }
 
    @Override
    public String getPassword() {
        return user.getPassword();
    }
 
    @Override
    public String getUsername() {
        return user.getEmail();
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
        if (user.getVerCode().equals("VERIFIED")) {
            return true;
        } else {
            System.out.println(user.getVerCode());
            return false;
        }
    }
     
    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }
 
}