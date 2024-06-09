package com.study.securitypjt.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.study.securitypjt.config.auth.PrincipalDetails;
import com.study.securitypjt.domain.User;
import com.study.securitypjt.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	// 구글로 받은 userRequest 데이터에 대한 후처리 되는 메소드
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// 구글 로그인을 완료하면 code를 리턴(OAuth-Client 라이브러리) -> AccessToken 요청 
		// userRequest 정보 -> loadUser 메소드 호출 -> 구글로부터 회원 프로필을 받아옴 
		// System.out.println("getAccessToken: "+userRequest.getAccessToken() );
		// System.out.println("getClientRegistration: "+userRequest.getClientRegistration() );
		// System.out.println("getAttribute: "+super.loadUser(userRequest).getAttributes());
		// registrationId 로 어떤 OAuth로 로그인했는지 확인 가능
		
		// username = google_{sub}
		// password = 암호화(겟인데어)
		// provider = google
		// providerId = {sub}
		
		OAuth2User oauth2User = super.loadUser(userRequest);

		String provider = userRequest.getClientRegistration().getClientId();
		String providerId = oauth2User.getAttribute("sub");
		String username = provider+"_"+providerId;
		String password = bCryptPasswordEncoder.encode("겟인데어");
		String email = oauth2User.getAttribute("email");		
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			// 회원가입 진행
			userEntity = User.builder()
					.username(username)
					.password(password)
					.provider(providerId)
					.providerId(providerId)
					.email(email)
					.role(role)
					.build();
			
			userRepository.save(userEntity);
		}
		
		return new PrincipalDetails(userEntity);
	}
}
