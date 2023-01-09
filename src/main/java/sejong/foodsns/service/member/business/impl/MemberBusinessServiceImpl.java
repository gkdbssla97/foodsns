package sejong.foodsns.service.member.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.ReportMember;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.member.BlackListRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.repository.member.ReportMemberRepository;
import sejong.foodsns.service.member.business.MemberBusinessService;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberBusinessServiceImpl implements MemberBusinessService {

    private final ReportMemberRepository reportMemberRepository;
    private final MemberRepository memberRepository;
    private final BlackListRepository blackListRepository;

    /**
     * 회원 등급 서비스
     * @param memberRequestDto
     * @return 회원 정보, OK
     */
    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberRankService(MemberRequestDto memberRequestDto) {

        Optional<Member> member = getMember(memberRequestDto);

        getMember(member).memberRankUp(getMember(member).getRecommendCount());

        Member save = memberRepository.save(getMember(member));

        MemberResponseDto memberResponseDto = getMemberResponseDto(save);

        return new ResponseEntity<>(memberResponseDto, OK);
    }

    /**
     * 신고 회원 카운트
     * @param memberRequestDto
     * @return 회원 정보, OK
     */
    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberReportCount(MemberRequestDto memberRequestDto) {
        Optional<Member> member = getMember(memberRequestDto);

        getMember(member).reportCount();

        Member save = memberRepository.save(getMember(member));

        MemberResponseDto memberResponseDto = getMemberResponseDto(save);

        return new ResponseEntity<>(memberResponseDto, OK);
    }

    /**
     * 블랙 리스트 회원 패널티 카운트
     * @param memberRequestDto
     * @return 회원 정보, OK
     */
    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberBlackListPenaltyCount(MemberRequestDto memberRequestDto) {

        Optional<Member> member = getMember(memberRequestDto);

        ReportMember.blackListPenaltyCount(getMember(member));

        Member save = memberRepository.save(getMember(member));

        MemberResponseDto memberResponseDto = getMemberResponseDto(save);

        return new ResponseEntity<>(memberResponseDto, OK);
    }

    /**
     * 회원 추천 수 업데이트 (게시물에 받은 추천수를 맴버로 업데이트 {Mapping} 초기 구현)
     * @param memberRequestDto
     * @param recommend
     * @return 회원 정보, OK
     */
    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> memberRecommendUpdate(MemberRequestDto memberRequestDto, int recommend) {

        Optional<Member> memberRequest = getMember(memberRequestDto);

        getMember(memberRequest).memberRecommendCount(recommend);

        Member save = memberRepository.save(getMember(memberRequest));

        MemberResponseDto memberResponseDto = getMemberResponseDto(save);

        return new ResponseEntity<>(memberResponseDto, OK);
    }

    /**
     * Optional 상태에서 객체 꺼내기
     * @param member
     * @return 회원 객체
     */
    private Member getMember(Optional<Member> member) {
        return member.get();
    }

    /**
     * 회원 응답 Dto 로 변형
     * @param save
     * @return 회원 응답 Dto
     */
    private MemberResponseDto getMemberResponseDto(Member save) {
        return MemberResponseDto.builder()
                .member(save)
                .build();
    }

    /**
     * 회원 찾기
     * @param memberRequestDto
     * @return Optional Member
     */
    private Optional<Member> getMember(MemberRequestDto memberRequestDto) {
        return ofNullable(memberRepository.findMemberByEmail(memberRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다.")));
    }
}
