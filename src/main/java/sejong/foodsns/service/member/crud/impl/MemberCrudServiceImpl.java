package sejong.foodsns.service.member.crud.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.config.security.WebSecurityConfig;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.crud.MemberCrudService;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCrudServiceImpl implements MemberCrudService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 생성 -> 성공 201, 실패 404
     * @param memberRequestDto
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberCreate(MemberRequestDto memberRequestDto) {

        Member member = Member.builder()
                .username(memberRequestDto.getUsername())
                .email(memberRequestDto.getEmail())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .memberType(NORMAL)
                .build();

        Member save = memberRepository.save(member);
        return new ResponseEntity<>(new MemberResponseDto(save), CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberPasswordUpdate(MemberRequestDto memberRequestDto, String password) {

        Optional<Member> member = ofNullable(memberRepository.findById(memberRequestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다.")));

        Member updateMember = getMember(member).memberPasswordUpdate(passwordEncoder.encode(password));

        return new ResponseEntity<>(new MemberResponseDto(updateMember), OK);
    }

    /**
     * 회원 이름 수정 -> 성공 200, 실패 404
     * @param memberRequestDto
     * @param username
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberNameUpdate(MemberRequestDto memberRequestDto, String username) {

        Optional<Member> member = ofNullable(memberRepository.findById(memberRequestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다.")));

        Member updateMember = getMember(member).memberNameUpdate(username);

        return new ResponseEntity<>(new MemberResponseDto(updateMember), OK);
    }

    /**
     * 회원 탈퇴 -> 성공 200, 실패 404
     * @param memberRequestDto
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberDelete(MemberRequestDto memberRequestDto) {

        Optional<Member> member = ofNullable(memberRepository.findById(memberRequestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다.")));

        memberRepository.delete(getMember(member));

        return new ResponseEntity<>(OK);
    }

    /**
     * 회원 찾기 -> 성공 200, 실패 404
     * @param memberRequestDto
     * @return
     */
    @Override
    public ResponseEntity<MemberResponseDto> findMember(MemberRequestDto memberRequestDto) {

        Optional<Member> member = ofNullable(memberRepository.findById(memberRequestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다.")));

        return new ResponseEntity<>(new MemberResponseDto(getMember(member)), OK);
    }

    /**
     * 맴버 목록 -> 성공 200
     * @return
     */
    @Override
    public ResponseEntity<List<MemberResponseDto>> memberList() {

        List<Member> members = memberRepository.findAll();

        List<MemberResponseDto> collect = members.stream()
                .map(MemberResponseDto::new)
                .collect(toList());

        return new ResponseEntity<>(collect, OK);
    }

    /**
     * Optional Member -> return member
     * @param member
     * @return
     */
    private Member getMember(Optional<Member> member) {
        return member.get();
    }
}
