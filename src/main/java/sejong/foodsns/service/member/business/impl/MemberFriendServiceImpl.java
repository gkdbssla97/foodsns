package sejong.foodsns.service.member.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Friend;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.member.FriendRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.business.MemberFriendService;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static sejong.foodsns.domain.member.MemberType.BLACKLIST;
import static sejong.foodsns.service.member.crud.MemberSuccessOrFailedMessage.FRIEND_DELETE_FAILED;
import static sejong.foodsns.service.member.crud.MemberSuccessOrFailedMessage.FRIEND_SEARCH_FAILED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFriendServiceImpl implements MemberFriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    /**
     * 친구 추가
     * @param memberRequestDto 회원(본인) 요청 Dto
     * @param username 찾으려는 친구 닉네임
     * @return 회원 응답 Dto (본인)
     */
    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> friendMemberAdd(MemberRequestDto memberRequestDto, String username) {
        Optional<Member> member = memberRepository.findMemberByEmail(memberRequestDto.getEmail());
        Optional<Member> friendSearch = memberRepository.findMemberByEmail(username);

        // 추가하려는 회원이 블랙리스트가 아니라면
        if(!getMember(friendSearch).getMemberType().equals(BLACKLIST)) {
            // 친구 추가
            getMember(member).setFriends(new Friend(getMember(friendSearch)));

            return new ResponseEntity<>(getMemberResponseDto(member), CREATED);
        } else
            // 추가하는 회원이 블랙리스트이면 예외 발생
            throw new IllegalStateException("블랙리스트인 회원을 친구 추가 할 수 없습니다.");
    }

    /**
     * 친구 삭제
     * @param memberRequestDto 회원(본인) 요청 Dto
     * @param index 회원의 친구 리스트 index
     * @return 회원 응답 Dto (본인)
     */
    @Override
    @Transactional
    public ResponseEntity<MemberResponseDto> friendMemberDelete(MemberRequestDto memberRequestDto, int index) {
        Optional<Member> member = memberRepository.findMemberByUsername(memberRequestDto.getEmail());

        // 삭제 완료
        try {
            getMember(member).getFriends().remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(FRIEND_DELETE_FAILED);
        }

        MemberResponseDto memberResponseDto = getMemberResponseDto(member);

        return new ResponseEntity<>(memberResponseDto, OK);
    }

    /**
     * 친구 목록
     * @param memberRequestDto 회원(본인) 요청 Dto
     * @return 회원 응답 Dto List (친구 리스트)
     */
    @Override
    public ResponseEntity<List<MemberResponseDto>> friendMemberList(MemberRequestDto memberRequestDto) {

        Optional<Member> member = memberRepository.findMemberByEmail(memberRequestDto.getEmail());

        List<MemberResponseDto> collect = friendsMappedMemberResponseDtos(member);

        return new ResponseEntity<>(collect, OK);
    }

    /**
     * 친구 상세 조회
     * @param memberRequestDto 회원(본인) 요청 Dto
     * @param index 회원의 친구 리스트 index
     * @return 회원 응답 Dto
     */
    @Override
    public ResponseEntity<MemberResponseDto> friendMemberDetailSearch(MemberRequestDto memberRequestDto, int index) {

        Optional<Member> member = memberRepository.findMemberByUsername(memberRequestDto.getEmail());
        try {
            Friend friend = getMember(member).getFriends().get(index);

            MemberResponseDto memberResponseDto = getMemberResponseDto(friend);

            return new ResponseEntity<>(memberResponseDto, OK);

        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(FRIEND_SEARCH_FAILED);
        }
    }

    /**
     * 친구 리스트를 회원 응답 Dto 로 매핑 (Convert)
     * @param member 회원
     * @return 회원 응답 Dto List
     */
    private List<MemberResponseDto> friendsMappedMemberResponseDtos(Optional<Member> member) {
        List<Friend> friends = getMember(member).getFriends();

        return friends.stream().map(friend -> new MemberResponseDto(friend.getMember()))
                .collect(toList());
    }

    /**
     * 친구를 회원 응답 Dto로 매핑
     * @param friend 친구 객체
     * @return 회원 응답 Dto
     */
    private static MemberResponseDto getMemberResponseDto(Friend friend) {
        return MemberResponseDto.builder()
                .member(friend.getMember())
                .build();
    }


    private static MemberResponseDto getMemberResponseDto(Optional<Member> member) {
        return MemberResponseDto.builder()
                .member(getMember(member))
                .build();
    }

    /**
     * Optional Member peel off the wrapping
     * @param member 회원
     * @return 회원
     */
    private static Member getMember(Optional<Member> member) {
        return member.get();
    }
}
