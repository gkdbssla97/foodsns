package sejong.foodsns.service.member.login.jwt.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.login.MemberLoginDto;
import sejong.foodsns.dto.token.TokenResponseDto;
import sejong.foodsns.jwt.JwtProvider;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.login.MemberLoginAndLogoutMessage;
import sejong.foodsns.service.member.login.jwt.MemberLoginService;
import sejong.foodsns.service.redis.RedisService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.service.member.login.MemberLoginAndLogoutMessage.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberLoginServiceImpl implements MemberLoginService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<String> jwtLogin(MemberLoginDto loginDto)
            throws JsonProcessingException {

        Optional<Member> member = memberRepository.findMemberByEmail(loginDto.getEmail());
        String password = getMember(member).getPassword();

        boolean matchCheck = passwordEncoder.matches(loginDto.getPassword(), password);

        if(matchCheck) {
            TokenResponseDto token = jwtProvider.createTokenByLogin(loginDto.getEmail());
            return new ResponseEntity<>(token.getAccessToken(), OK);
        } else {
            throw new IllegalArgumentException(LOGIN_FAIL);
        }
    }

    @Override
    public ResponseEntity<String> jwtLogout(String email, HttpServletRequest request) {
        String requestHeader = request.getHeader("X-AUTH-TOKEN");
        jwtProvider.logout(email, requestHeader);

        return new ResponseEntity<>(LOGOUT_SUCCESS, OK);
    }

    private Member getMember(Optional<Member> member) {
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new IllegalArgumentException("존재하지 않는 이메일입니다.");
        }
    }
}
