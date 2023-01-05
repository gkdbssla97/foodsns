package sejong.foodsns.service.member.login;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.login.MemberLoginDto;

import javax.servlet.http.HttpServletRequest;

public interface MemberLoginService {

    ResponseEntity<String> login(MemberLoginDto memberLoginDto, HttpServletRequest request);

    ResponseEntity<String> logout(HttpServletRequest request);

    ResponseEntity<String> keepSessionLogin(Member member);
}
