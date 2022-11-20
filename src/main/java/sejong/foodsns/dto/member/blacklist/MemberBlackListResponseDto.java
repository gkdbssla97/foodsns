package sejong.foodsns.dto.member.blacklist;

import lombok.*;
import sejong.foodsns.domain.member.BlackList;
import sejong.foodsns.domain.member.ReportMember;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberBlackListResponseDto {

    private Long id;
    private String reason;
    private ReportMember reportMember;

    @Builder
    public MemberBlackListResponseDto(BlackList blackList) {
        this.id = blackList.getId();
        this.reason = blackList.getReason();
        this.reportMember = blackList.getReportMember();
    }
}
