package sejong.foodsns.service.board.crud;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.domain.board.SearchOption;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BoardCrudService {

    ResponseEntity<Optional<BoardResponseDto>> boardCreate(BoardRequestDto boardRequestDto) throws IOException;

    ResponseEntity<Optional<BoardResponseDto>> boardTitleUpdate(String updateTitle, String orderTitle);

    ResponseEntity<Optional<BoardResponseDto>> boardDelete(BoardRequestDto boardRequestDto);

    ResponseEntity<Optional<BoardResponseDto>> findBoard(String title);

    ResponseEntity<List<BoardResponseDto>> search(SearchOption searchOption, String content);

    ResponseEntity<Optional<List<BoardResponseDto>>> boardList();

    Boolean boardTitleExistValidation(BoardRequestDto boardRequestDto);

}
