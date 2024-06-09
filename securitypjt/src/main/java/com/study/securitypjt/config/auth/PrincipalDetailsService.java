package com.study.securitypjt.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.study.securitypjt.domain.User;
import com.study.securitypjt.repository.UserRepository;

// Security 설정에서 loginProcessingUrl("/login")
// 해당 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는
// loadUserByUsername 이 호출 된다 -> 규칙임 
@Service
public class PrincipalDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	// SecuritySession(Authentication(UserDetails))
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 여기서 username은 loginForm의 username
		// username2로 바꾸고 싶다면 SecurityConfig에서 바꿔줘야한다.
		User userEntity = userRepository.findByUsername(username);
		if(userEntity != null) {
			return new PrincipalDetails(userEntity);
		}
		return null;
	}

}
