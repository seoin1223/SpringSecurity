package com.cos.security1.config.auth;

/*
 시큐리티가 /login을 낚아채서 로그인을 진행
 로그인의 진행이 완료가 되면 시큐리티 session을 만들어 줌 (Security ContextHolder) : key
 object => Authentication 타입 객체
 Authentication 안에 User 정보가 있어야 됨
 UserObject type => UserDetails Type 객체


 Security Session => Authentication => UserDetails(PrincipalDetails)


 */


import com.cos.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private User user; // Composition

    public PrincipalDetails(User user) {
        this.user = user;
    }


    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
        // 1년 동안 회원이 로그인을 안하면 휴면계정으로 변경
        /*
        * user.getLoginDate()
        * // 현재 시간 - LoginDate() = > 1년을 초과하면 refurn false;
        *
        *
        * */

        return true;
    }
}
