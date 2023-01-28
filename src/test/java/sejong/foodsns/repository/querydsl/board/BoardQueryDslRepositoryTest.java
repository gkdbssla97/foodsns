package sejong.foodsns.repository.querydsl.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.QBoard;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.domain.member.QMember;
import sejong.foodsns.repository.board.BoardRepository;
import sejong.foodsns.repository.member.MemberRepository;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static sejong.foodsns.domain.member.MemberType.*;

@DataJpaTest
public class BoardQueryDslRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardRepository boardRepository;

    private JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    void init() {
        jpaQueryFactory = new JPAQueryFactory(em);

        Member member = new Member("윤광오", "swager253@fdsfds.com", "qwer1234@!", NORMAL);
        Board board = new Board("안녕하세요", "안녕하세요", member.getMemberRank(),
                20L, 20, null, member);
        Board board1 = new Board("안녕하세요", "안녕하세요", member.getMemberRank(),
                20L, 20, null, member);
        Board board2 = new Board("안녕하세요", "안녕하세요", member.getMemberRank(),
                20L, 20, null, member);

        memberRepository.save(member);
        boardRepository.save(board);
        boardRepository.save(board1);
        boardRepository.save(board2);
    }

    @Test
    @DisplayName("회원의 어떤 게시물을 작성했는지 확인하는 테스트")
    void memberBoardCreateCheck() {
        // given
        String email = "swager253@fdsfds.com";
        String username = "윤광오";
        QBoard qBoard = new QBoard("b");

        // when
        // 회원 닉네임으로 조회
        List<Board> boards = jpaQueryFactory.selectFrom(qBoard)
                .where(qBoard.member.username.eq(username))
                .fetch();

        // 회원 이메일로 조회
        List<Board> boards1 = jpaQueryFactory.selectFrom(qBoard)
                .where(qBoard.member.email.eq(email))
                .fetch();

        // then
        assertThat(boards.size()).isEqualTo(3);
        assertThat(boards1.size()).isEqualTo(3);
    }
}
