package com.study.securitypjt.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.study.securitypjt.domain.User;

// security가 /login으로 들어오면 낚아채서 로그인 진행
// 로그인 진행이 완료가 되면 security session을 만들어준다.
// Security ContextHolder 라는 키 값으로 세션쪽에 저장
// 세션에 들어갈 수 있는 객체가 정해져 있음 => Authentication 객체
// Authentication 안에는 UserDetails 객체
// Security Session > Authentication > UserDeails
public class PrincipalDetails implements UserDetails{

	private User user;
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	// 해당 User의 권한을 리턴
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

}
