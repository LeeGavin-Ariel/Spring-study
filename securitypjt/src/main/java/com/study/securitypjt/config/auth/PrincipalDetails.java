package com.study.securitypjt.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.study.securitypjt.domain.User;

import lombok.Data;

// security가 /login으로 들어오면 낚아채서 로그인 진행
// 로그인 진행이 완료가 되면 security session을 만들어준다.
// Security ContextHolder 라는 키 값으로 세션쪽에 저장
// 세션에 들어갈 수 있는 객체가 정해져 있음 => Authentication 객체
// Authentication 안에는 UserDetails 객체
// Security Session > Authentication > UserDeails

// PrincipalDetails 를 만든 이유
// Security Session에 들어갈 수 있는 객체는 Authentication 객체 밖에 없다.
// Authentication 안에는 UserDetails, OAuth2User 타입이 들어가야한다. 
// 두 가지 타입을 implements로 묶어서 User 객체를 품기 위해서이다. 
@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

	private User user;
	private Map<String, Object> attributes;
	
	// 일반 로그인 생성자 
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	// OAuth 로그인 생성자
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
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

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}

}
