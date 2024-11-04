package com.choongang.frombirth_backend.security;

import com.choongang.frombirth_backend.model.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private final Users user;

    public UserPrincipal(Users user) {
        this.user = user;
    }

    public Integer getUserId() {
        return user.getUserId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public Long getKakaoId() {
        return user.getKakaoId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한이 없다면 빈 리스트 반환
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        // 패스워드를 사용하지 않으므로 null 또는 빈 문자열 반환
        return "";
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 필요에 따라 구현
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 필요에 따라 구현
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 필요에 따라 구현
    }

    @Override
    public boolean isEnabled() {
        return true; // 필요에 따라 구현
    }
}
