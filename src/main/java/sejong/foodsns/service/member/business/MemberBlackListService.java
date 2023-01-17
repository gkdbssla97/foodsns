package sejong.foodsns.service.member.business;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListCreateRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListDetailDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;

import java.util.List;
import java.util.Optional;

public interface MemberBlackListService {

    ResponseEntity<MemberBlackListResponseDto> blackListMemberCreate(String reason, String email);

    ResponseEntity<MemberBlackListResponseDto> blackListMemberFindOne(Long id);

    ResponseEntity<List<MemberBlackListResponseDto>> blackListMemberList();

    ResponseEntity<MemberBlackListDetailDto> blackListMemberDetailSearch(Long id);

    default ResponseEntity<MemberBlackListResponseDto> blackListMemberBoardDelete() {
        return null;
    }
}
